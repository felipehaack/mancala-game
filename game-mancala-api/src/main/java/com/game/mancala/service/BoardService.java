package com.game.mancala.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.game.mancala.exception.MancalaException;
import com.game.mancala.model.Board;
import com.game.mancala.model.BoardWrapper;
import com.game.mancala.monad.Try;

@Service
public class BoardService {

    private final HashMap<UUID, Board> boards;

    public BoardService() {
        this.boards = new HashMap<>();
    }

    /**
     * This method will create a new board and add it to the boards hash map
     *
     * @param input a board create object
     * @return a board result
     */
    public BoardWrapper.Result create(
                    final BoardWrapper.Create input
    ) {
        final Board board = new Board(input.getBoardSize(), input.getStonesPerPit());
        boards.put(board.getId(), board);
        return toBoardResult(board);
    }

    /**
     * This method will look for a board by ID
     *
     * @param id the board ID
     * @return a board result as a try
     */
    public Try<BoardWrapper.Result> get(
                    final UUID id
    ) {
        return getOne(id).map(this::toBoardResult);
    }

    /**
     * This method will retrieve a board from the board hash map
     * Verify if the match still open and update the board
     *
     * @param id          the board ID
     * @param targetIndex the index to start updating the board
     * @return the board result as a try
     */
    public Try<BoardWrapper.Result> update(
                    final UUID id,
                    final Integer targetIndex
    ) {
        return getOne(id).map(board -> {
            if (board.getIsOpen().get())
                return board.update(targetIndex);
            else
                throw MancalaException.invalid("board.open");
        }).map(Board::processPossibleWinner).map(this::toBoardResult);
    }

    /**
     * This method is a helper to parse a board to a board result object
     *
     * @param board the current board
     * @return a board result object
     */
    private BoardWrapper.Result toBoardResult(
                    final Board board
    ) {
        final List<Integer> boardAsInteger = board.getBoard().stream()
                        .map(AtomicInteger::get)
                        .collect(Collectors.toList());

        return BoardWrapper.Result.builder()
                        .id(board.getId())
                        .board(boardAsInteger)
                        .currentPlayer(board.getCurrentPlayer().get())
                        .boardSizePerPlayer(board.getBoardSizePerPlayer())
                        .isOpen(board.getIsOpen().get())
                        .winner(board.getWinner().get())
                        .build();
    }

    /**
     * This is a private method to retrieve a possible board by ID
     *
     * @param id the board ID
     * @return a board as a try
     */
    private Try<Board> getOne(
                    UUID id
    ) {
        return Try.ofFailable(() -> {
            return Optional.ofNullable(boards.get(id))
                            .orElseThrow(() -> MancalaException.notFound("board"));
        });
    }
}
