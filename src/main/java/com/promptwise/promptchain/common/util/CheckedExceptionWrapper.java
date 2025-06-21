package com.promptwise.promptchain.common.util;

import org.springframework.util.Assert;

public class CheckedExceptionWrapper extends RuntimeException {
  private CheckedExceptionWrapper(Exception cause) {
    super(cause);
  }

  public static CheckedExceptionWrapper create(Exception cause) {
    Assert.notNull(cause, "The parameter cause cannot be null!");
    return new CheckedExceptionWrapper(cause);
  }
}