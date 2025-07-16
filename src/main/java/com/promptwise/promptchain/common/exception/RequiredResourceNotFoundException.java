package com.promptwise.promptchain.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;

/**
 * The word 'Required' in the class name signifies that is a non-recoverable exception and will never need to be
 * caught and handled by the App, and instead, will always be reported to the client (i.e. a RuntimeException by
 * definition).
 */
public class RequiredResourceNotFoundException extends Exception {

  @Serial
  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = LoggerFactory.getLogger(RequiredResourceNotFoundException.class);

  protected RequiredResourceNotFoundException(String message) {
    super(message);
  }

  public static RequiredResourceNotFoundException create(String resourceName, Integer resourceId) {
    return new RequiredResourceNotFoundException(String.format(
            "The '%s' having ID: '%d' does not exist!", resourceName, resourceId));
  }

}
