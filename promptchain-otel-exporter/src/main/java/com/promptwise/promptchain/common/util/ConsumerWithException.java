package com.promptwise.promptchain.common.util;

import java.util.function.Consumer;

@FunctionalInterface
public interface ConsumerWithException<T, E extends Exception> extends Consumer<T> {
  void acceptWithException(T t) throws E;

  @Override
  default void accept(T t) {
    try {
      acceptWithException(t);
    } catch (Exception e) {
      throw CheckedExceptionWrapper.create(e);
    }
  }
}
