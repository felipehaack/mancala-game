package com.game.mancala.model;

import java.util.concurrent.ThreadLocalRandom;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlayerKind {
    PLAYER_1(0),
    PLAYER_2(1);
    private int index;

    /**
     * This method is a helper to randomize a player using the thread local random to not instantiate a random object
     *
     * @return a randomized player
     */
    public static PlayerKind random() {
        final PlayerKind[] players = PlayerKind.values();
        final int randomPlayerIndex = ThreadLocalRandom.current().nextInt(0, players.length);
        return players[randomPlayerIndex];
    }

    /**
     * This method is a helper to provide the next player based on the current player
     *
     * @param currentPlayer the current player
     * @return the new player
     */
    public static PlayerKind nextPlayer(
                    final PlayerKind currentPlayer
    ) {
        final int nextPlayerIndex = currentPlayer.getIndex() + 1;
        if (nextPlayerIndex < PlayerKind.values().length)
            return PlayerKind.values()[nextPlayerIndex];
        return PlayerKind.PLAYER_1;
    }
}
