package com.promptwise.promptchain.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;

public class DatabaseAccessException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseAccessException.class);

  public static DatabaseAccessException create(Throwable cause) {
    return new DatabaseAccessException("An exception has occurred while performing a database operation!", cause);
  }

  private DatabaseAccessException(String message, Throwable cause) {
    super(message, cause);
  }

}
