package com.game.mancala.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.NONE)
class BoardPlayerCorner {
    private final Integer start;
    private final Integer end;

    /**
     * This constructor will create a board player corner to define the limits of the board for each player
     *
     * @param player             the current player
     * @param boardSizePerPlayer the size of the board for each player
     */
    BoardPlayerCorner(
                    final PlayerKind player,
                    final Integer boardSizePerPlayer
    ) {
        this.start = boardSizePerPlayer * player.getIndex();
        this.end = start + boardSizePerPlayer;
    }
}
