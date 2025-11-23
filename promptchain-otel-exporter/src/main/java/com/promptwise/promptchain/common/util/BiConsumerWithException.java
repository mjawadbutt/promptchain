package com.promptwise.promptchain.common.util;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface BiConsumerWithException<T, U, E extends Exception> extends BiConsumer<T, U> {
  void acceptWithException(T t, U u) throws E;

  @Override
  default void accept(T t, U u) {
    try {
      acceptWithException(t, u);
    } catch (Exception e) {
      throw CheckedExceptionWrapper.create(e);
    }
  }
}
