package com.promptwise.promptchain.common.util;

import java.util.function.BiFunction;

@FunctionalInterface
public interface BiFunctionWithException<T, U, R, E extends Exception> extends BiFunction<T, U, R> {

  R applyWithException(T t, U u) throws E;

  @Override
  default R apply(T t, U u) {
    try {
      return applyWithException(t, u);
    } catch (Exception e) {
      throw CheckedExceptionWrapper.create(e);
    }
  }
}