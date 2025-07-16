package com.promptwise.promptchain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;

public class InvalidApplicationConfigurationException extends Exception {

  @Serial
  private static final long serialVersionUID = 1L;

  private static final Logger LOGGER = LoggerFactory.getLogger(InvalidApplicationConfigurationException.class);

  public InvalidApplicationConfigurationException(String message) {
    this(message, null);
  }

  public InvalidApplicationConfigurationException(String message, Throwable cause) {
    super(message, cause);
  }

}
