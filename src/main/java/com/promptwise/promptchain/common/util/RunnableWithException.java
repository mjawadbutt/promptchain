package com.promptwise.promptchain.common.util;

@FunctionalInterface
public interface RunnableWithException<E extends Exception> {

  void run() throws E;

}
