package com.promptwise.promptchain.common.util;

import java.util.Objects;

public class CheckedExceptionWrapper extends RuntimeException {
  private CheckedExceptionWrapper(Exception cause) {
    super(cause);
  }

  public static CheckedExceptionWrapper create(Exception cause) {
    Objects.requireNonNull(cause, "The parameter 'cause' cannot be 'null'!");
    return new CheckedExceptionWrapper(cause);
  }
}