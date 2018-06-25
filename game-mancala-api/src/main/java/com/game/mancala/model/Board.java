package com.game.mancala.model;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.codepoetics.protonpack.Indexed;
import com.codepoetics.protonpack.StreamUtils;
import com.game.mancala.exception.MancalaException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Board {
    private final UUID id = UUID.randomUUID();
    private final Integer boardSizePerPlayer;
    private final List<AtomicInteger> board;
    private final AtomicReference<PlayerKind> currentPlayer;
    private final AtomicBoolean isOpen;
    private final AtomicReference<PlayerKind> winner;

    public Board(
                    final Integer boardSizePerPlayer,
                    final Integer stonesPerField
    ) {
        this.currentPlayer = new AtomicReference<>(PlayerKind.random());
        this.board = buildGameBoard(boardSizePerPlayer, stonesPerField);
        this.boardSizePerPlayer = boardSizePerPlayer + 1;
        this.isOpen = new AtomicBoolean(true);
        this.winner = new AtomicReference<>();
    }

    /**
     * This method will create a board for each player
     *
     * @param boardSizePerPlayer the size of the player board
     * @param stonesPerField     the amount of the stones in each pit
     * @return a list of pits with stones
     */
    private List<AtomicInteger> buildGameBoard(
                    final Integer boardSizePerPlayer,
                    final Integer stonesPerField
    ) {
        final Integer totalPlayers = PlayerKind.values().length;
        return Stream.generate(() -> buildPlayerBoard(boardSizePerPlayer, stonesPerField))
                        .limit(totalPlayers)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
    }

    /**
     * This method will create a board for only one player.
     * The first position of the list is for the collector
     *
     * @param boardSizePerPlayer the size of the player board
     * @param stonesPerField     the amount of the stones in each pit
     * @return a list of pits for only one player board
     */
    private List<AtomicInteger> buildPlayerBoard(
                    final Integer boardSizePerPlayer,
                    final Integer stonesPerField
    ) {
        return Stream.of(
                        Stream.of(new AtomicInteger(0)),
                        Stream.generate(() -> new AtomicInteger(stonesPerField)).limit(boardSizePerPlayer)
        ).flatMap(Function.identity()).collect(Collectors.toList());
    }

    /**
     * This method will update the board starting from the target index and through the others pits
     * Before update the board, it will execute a pre-check rules
     * The result of the update will be a new board with the same player or next one
     *
     * @param targetIndex the target index to start updating the board
     * @return the board itself
     * @throws MancalaException
     */
    public Board update(
                    final Integer targetIndex
    ) throws MancalaException {
        final BoardPlayerCorner corner = new BoardPlayerCorner(currentPlayer.get(), boardSizePerPlayer);

        if (!BoardRules.isIndexInTheInterval(targetIndex, corner.getStart(), corner.getEnd()))
            throw MancalaException.invalid("player.board.interval");

        if (BoardRules.isIndexWithStones(board, targetIndex, 0))
            throw MancalaException.invalid("player.board.field.zero");

        final Integer actualStones = BoardRules.getStonesAndSetZero(board, targetIndex);
        final Integer lastIndex = loopUpdateBoard(targetIndex, actualStones);

        if (BoardRules.isIndexInTheInterval(lastIndex, corner.getStart(), corner.getEnd()) &&
                        BoardRules.isIndexWithStones(board, lastIndex, 1)) {
            BoardRules.transferStones(board, corner.getStart(), lastIndex);
        }

        if (BoardRules.doNotHaveOtherRound(currentPlayer.get(), lastIndex, boardSizePerPlayer)) {
            currentPlayer.updateAndGet(PlayerKind::nextPlayer);
        }

        return this;
    }

    /**
     * This method will keep executing until run out the stones (stones <= 0)
     * Each pit will be incremented in 1
     *
     * @param currentIndex the current index in the board
     * @param stonesLeft   the stones left to keep moving the cursor
     * @return the last index in the board
     */
    private Integer loopUpdateBoard(
                    final Integer currentIndex,
                    final Integer stonesLeft
    ) {
        if (stonesLeft > 0) {
            final Integer nextIndex = moveCursor(currentIndex - 1);
            if (BoardRules.isOtherPlayerCollector(currentPlayer.get(), boardSizePerPlayer, nextIndex)) {
                return loopUpdateBoard(nextIndex, stonesLeft);
            } else {
                board.get(nextIndex).incrementAndGet();
                return loopUpdateBoard(nextIndex, stonesLeft - 1);
            }
        } else
            return currentIndex;
    }

    /**
     * This method is a helper to move the cursor through the board without worry about Range Index Exception
     *
     * @param index the current index
     * @return the new index
     */
    private Integer moveCursor(
                    final Integer index
    ) {
        if (index < 0)
            return board.size() - 1;
        else if (index >= board.size())
            return 0;
        else
            return index;
    }

    /**
     * This method will execute a combination of the detecting if the game was finished and
     * then applying to the collector method to collect all remaining stones to the right collector field and
     * then defining the winner of the game
     *
     * @return a updated board
     * @throws MancalaException
     */
    public Board processPossibleWinner() throws MancalaException {
        if (isMatchEnded()) {
            final List<Integer> collectedStones = collectAllRemainStones();
            final PlayerKind winnerInBoard = findWinnerInBoard(collectedStones)
                            .orElseThrow(() -> MancalaException.invalid("board.player.winner"));
            winner.set(winnerInBoard);
            isOpen.set(false);
        }

        return this;
    }

    /**
     * This method will look for the player board that run out of the stones to decides if the match was finished
     *
     * @return true if the match finished
     */
    private Boolean isMatchEnded() {
        final Predicate<AtomicInteger> isEqualsZero = (pit) -> pit.get() == 0;
        return Stream.of(PlayerKind.values()).map(player -> {
            final BoardPlayerCorner boardPlayerCorner = new BoardPlayerCorner(player, boardSizePerPlayer);
            return board.subList(boardPlayerCorner.getStart() + 1, boardPlayerCorner.getEnd())
                            .stream()
                            .allMatch(isEqualsZero);
        }).anyMatch(Predicate.isEqual(true));
    }

    /**
     * This method will collect all remaining stones and move all to its collector pit
     *
     * @return a list of collectors with its collected stones
     */
    private List<Integer> collectAllRemainStones() {
        return Stream.of(PlayerKind.values()).map(player -> {
            final BoardPlayerCorner corner = new BoardPlayerCorner(player, boardSizePerPlayer);
            final Integer collectedStones = board.subList(corner.getStart() + 1, corner.getEnd()).stream()
                            .map(pit -> pit.getAndSet(0))
                            .reduce(0, (a, b) -> a + b);
            return board.get(corner.getStart()).updateAndGet(stones -> stones + collectedStones);
        }).collect(Collectors.toList());
    }

    /**
     * This method will look in the collected stones list for the winner
     *
     * @param collectedStones a list of collected stones for each player
     * @return the winner as Optional
     */
    private Optional<PlayerKind> findWinnerInBoard(
                    final List<Integer> collectedStones
    ) {
        return StreamUtils.zipWithIndex(collectedStones.stream())
                        .sorted(Comparator.comparing(Indexed::getValue))
                        .reduce((first, second) -> second)
                        .map(indexedPosition -> {
                            final Long indexedAsLong = indexedPosition.getIndex();
                            return PlayerKind.values()[indexedAsLong.intValue()];
                        });
    }
}
