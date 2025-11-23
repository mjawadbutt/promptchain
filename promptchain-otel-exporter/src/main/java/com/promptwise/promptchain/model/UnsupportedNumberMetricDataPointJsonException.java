package com.promptwise.promptchain.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;

public final class UnsupportedNumberMetricDataPointJsonException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 1L;

  private static final Logger LOGGER = LoggerFactory.getLogger(UnsupportedNumberMetricDataPointJsonException.class);


  private UnsupportedNumberMetricDataPointJsonException(final String message) {
    super(message);
  }

  public static UnsupportedNumberMetricDataPointJsonException create(final String message) {
    return new UnsupportedNumberMetricDataPointJsonException(message);
  }

}
