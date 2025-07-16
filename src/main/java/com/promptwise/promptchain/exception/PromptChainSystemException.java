package com.promptwise.promptchain.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;

public class PromptChainSystemException extends RuntimeException {

  private static final Logger LOGGER = LoggerFactory.getLogger(PromptChainSystemException.class);
  @Serial
  private static final long serialVersionUID = 1L;

  public static PromptChainSystemException create(String message) {
    return create(message, null);
  }

  public static PromptChainSystemException create(Throwable cause) {
    return create(null, cause);
  }

  public static PromptChainSystemException create(String message, Throwable cause) {
    if (message == null && cause == null) {
      LOGGER.warn("""
              Neither message nor cause have been specified while throwing this! 
              It is recommended to specify at least one of these to make it easier to debug the issue.
              """);
    }
    return new PromptChainSystemException(message, cause);
  }

  private PromptChainSystemException(String message, Throwable cause) {
    super(message, cause);
  }

}
