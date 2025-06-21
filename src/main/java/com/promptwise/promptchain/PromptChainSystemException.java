package com.promptwise.promptchain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;

public final class PromptChainSystemException extends RuntimeException {

  private static final Logger LOGGER = LoggerFactory.getLogger(LuvCommonLibSystemException.class);
  @Serial
  private static final long serialVersionUID = 1L;

  public static LuvCommonLibSystemException create(final String message) {
    return create(message, null);
  }

  public static LuvCommonLibSystemException create(final Throwable cause) {
    return create(null, cause);
  }

  public static LuvCommonLibSystemException create(final String message, final Throwable cause) {
    if (message == null && cause == null) {
      LOGGER.warn("""
              Neither message nor cause have been specified while throwing this exception! 
              It is recommended to specify at least one of these to make it easier to debug the issue.
              """);
    }
    return new LuvCommonLibSystemException(message, cause);
  }

  private PromptChainSystemException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
