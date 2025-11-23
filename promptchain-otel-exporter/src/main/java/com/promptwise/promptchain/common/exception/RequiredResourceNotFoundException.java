package com.promptwise.promptchain.common.exception;

import jakarta.validation.constraints.NotNull;
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

  public static RequiredResourceNotFoundException create(@NotNull final String resourceName,
                                                         @NotNull final String resourceIdKey,
                                                         @NotNull final String resourceIdValue) {
    return new RequiredResourceNotFoundException(String.format(
            "The '%s' having '%s': '%s' does not exists!", resourceName, resourceIdKey, resourceIdValue));
  }

}
