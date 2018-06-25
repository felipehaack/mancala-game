/*
 * Copyright (c) KLM Royal Dutch Airlines. All Rights Reserved.
 * ============================================================
 */

package com.game.mancala.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MancalaException extends RuntimeException {
    private String message;

    /**
     * This class holds all NOT_FOUND exceptions
     */
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public static class NotFoundFailure extends MancalaException {
        NotFoundFailure(final String message) {
            setMessage(message + ".not_found");
        }
    }

    /**
     * This class holds all BAD_REQUEST exceptions
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public static class InvalidFailure extends MancalaException {
        InvalidFailure(final String message) {
            setMessage(message + ".invalid");
        }
    }

    public static NotFoundFailure notFound(final String message) {
        return new NotFoundFailure(message);
    }

    public static InvalidFailure invalid(final String message) {
        return new InvalidFailure(message);
    }
}
