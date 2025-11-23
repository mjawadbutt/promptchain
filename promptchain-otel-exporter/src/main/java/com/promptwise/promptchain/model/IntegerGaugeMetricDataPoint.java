package com.promptwise.promptchain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class IntegerGaugeMetricDataPoint extends NumberMetricDataPoint {

  private final Integer value;

  public IntegerGaugeMetricDataPoint(
          final String timeUnixNano,
          @JsonProperty("startTimeUnixNano") final String startTimeUnixNano,
          @JsonProperty("attributes") final List<SignalAttribute> attributes,
          @JsonProperty("asInt") final Integer value) {
    super(timeUnixNano, startTimeUnixNano, attributes);
    this.value = value;
  }

  @JsonProperty("asInt")
  public Integer getValue() {
    return value;
  }

}
