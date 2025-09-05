package com.promptwise.promptchain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MetricType {
  GAUGE("gauge"),
  SUM("sum"),
  HISTOGRAM("histogram"),
  SUMMARY("summary");

  private final String value;

  MetricType(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @JsonCreator
  public static MetricType fromValue(String value) {
    for (MetricType t : values()) {
      if (t.value.equalsIgnoreCase(value)) {
        return t;
      }
    }
    throw new IllegalArgumentException("Unknown MetricType: " + value);
  }
}
