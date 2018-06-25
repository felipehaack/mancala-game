package com.game.mancala.model;

import java.util.List;
import java.util.UUID;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class BoardWrapper {
    @Data
    @NoArgsConstructor
    @Setter(AccessLevel.NONE)
    public static class Create {
        @Min(2)
        @Max(6)
        @NotNull
        private Integer boardSize;

        @Min(1)
        @Max(6)
        @NotNull
        private Integer stonesPerPit;
    }

    @Builder
    @Data
    @Setter(AccessLevel.NONE)
    public static class Result {
        private final UUID id;
        private final PlayerKind currentPlayer;
        private final Integer boardSizePerPlayer;
        private final Boolean isOpen;
        private final PlayerKind winner;
        private final List<Integer> board;
    }
}
