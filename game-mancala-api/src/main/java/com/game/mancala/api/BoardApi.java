package com.game.mancala.api;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.game.mancala.model.BoardWrapper;
import com.game.mancala.service.BoardService;

@CrossOrigin
@Controller
@RequestMapping("/board")
public class BoardApi {

    private final BoardService boardService;

    public BoardApi(final BoardService boardService) {
        this.boardService = boardService;
    }

    /**
     * This method will ask to the board service to create a new board
     *
     * @param input the board create object
     * @return a board result
     */
    @PostMapping
    public ResponseEntity<BoardWrapper.Result> create(
                    @RequestBody @Valid final BoardWrapper.Create input
    ) {
        final BoardWrapper.Result result = boardService.create(input);
        return ResponseEntity.ok(result);
    }

    /**
     * This method will ask to the board service for a existing board by ID
     *
     * @param id the board ID
     * @return a possible board result
     */
    @GetMapping("/{id}")
    public ResponseEntity<BoardWrapper.Result> get(
                    @PathVariable final UUID id
    ) {
        return boardService.get(id)
                        .map(ResponseEntity::ok)
                        .getUnchecked();
    }

    /**
     * This method will ask to the board service to update a board by ID starting from the index
     *
     * @param id    the board ID
     * @param index the index to start updating the board
     * @return a possible board result
     */
    @PutMapping("/{id}/target/{index}")
    public ResponseEntity<BoardWrapper.Result> target(
                    @PathVariable final UUID id,
                    @PathVariable final Integer index
    ) {
        return boardService.update(id, index)
                        .map(ResponseEntity::ok)
                        .getUnchecked();
    }
}
