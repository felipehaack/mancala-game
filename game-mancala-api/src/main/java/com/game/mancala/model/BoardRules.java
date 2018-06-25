package com.game.mancala.model;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Stream;

class BoardRules {

    /**
     * This method will check if the index is in the interval of the player board
     *
     * @param index           the current index
     * @param startBoardIndex the start index of the player board
     * @param endBoardIndex   the end index of the player board
     * @return true if the index is in the interval of start and end
     */
    public static Boolean isIndexInTheInterval(
                    final Integer index,
                    final Integer startBoardIndex,
                    final Integer endBoardIndex
    ) {
        return index > startBoardIndex && index < endBoardIndex;
    }

    /**
     * This method will compare the stones with the position in the board
     *
     * @param board  the current board
     * @param index  the current index
     * @param stones the stones to be compare to the board with index
     * @return true if the number in the board(index) is equals to stones
     */
    public static Boolean isIndexWithStones(
                    final List<AtomicInteger> board,
                    final Integer index,
                    final Integer stones
    ) {
        return board.get(index).get() == stones;
    }

    /**
     * This method will update the number in the board with Zero and return the old number
     *
     * @param board the current board
     * @param index the current index
     * @return the old number
     */
    public static Integer getStonesAndSetZero(
                    final List<AtomicInteger> board,
                    final Integer index
    ) {
        return board.get(index).getAndSet(0);
    }

    /**
     * This method will verify if the index stops in the its collector pit
     *
     * @param player             the current player
     * @param index              the current index
     * @param boardSizePerPlayer the size of the player board
     * @return true if the index is in its collector pit
     */
    public static Boolean doNotHaveOtherRound(
                    final PlayerKind player,
                    final Integer index,
                    final Integer boardSizePerPlayer
    ) {
        return index != boardSizePerPlayer * player.getIndex();
    }

    /**
     * This method is a helper to transfer all stones from pit to pit
     *
     * @param board           the current board
     * @param startBoardIndex the start index of the player
     * @param index           the current index
     */
    public static void transferStones(
                    final List<AtomicInteger> board,
                    final Integer startBoardIndex,
                    final Integer index
    ) {
        final Integer targetToLoseIndex = board.size() - index;
        final Integer stonesToTransfer = board.get(targetToLoseIndex).getAndSet(0);
        board.get(startBoardIndex).updateAndGet(stones -> stones + stonesToTransfer);
    }

    /**
     * This method will verify if the index stops in the other player collector pit
     *
     * @param player             the current player
     * @param boardSizePerPlayer the board size of the player
     * @param index              the current index
     * @return true if the index stops in the other player collector pit
     */
    public static Boolean isOtherPlayerCollector(
                    final PlayerKind player,
                    final Integer boardSizePerPlayer,
                    final Integer index
    ) {
        return Stream.of(PlayerKind.values())
                        .filter(e -> !e.equals(player))
                        .map(otherPlayer -> {
                            return boardSizePerPlayer * otherPlayer.getIndex() == index;
                        }).anyMatch(Predicate.isEqual(true));
    }
}
