package com.promptwise.promptchain.common.exception;

import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;

public class ResourceAlreadyExistsException extends Exception {

  @Serial
  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = LoggerFactory.getLogger(ResourceAlreadyExistsException.class);

  protected ResourceAlreadyExistsException(String message) {
    super(message);
  }

  public static ResourceAlreadyExistsException create(@NotNull final String resourceName,
                                                      @NotNull final String resourceIdKey,
                                                      @NotNull final String resourceIdValue) {
    return new ResourceAlreadyExistsException(String.format(
            "The '%s' having '%s': '%s' already exists!", resourceName, resourceIdKey, resourceIdValue));
  }

}
