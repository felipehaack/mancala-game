/*
 * Copyright (c) KLM Royal Dutch Airlines. All Rights Reserved.
 * ============================================================
 */

package com.game.mancala.monad;

/**
 * This is similar to the Java Supplier function type.
 * It has a checked exception on it to allow it to be used in lambda expressions on the Try monad.
 *
 * @param <T>
 * @param <R>
 */
public interface TryMapFunction<T, R> {
    R apply(T t) throws Throwable;
}
