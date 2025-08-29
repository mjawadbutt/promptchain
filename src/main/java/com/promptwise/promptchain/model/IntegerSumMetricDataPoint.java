package com.promptwise.promptchain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class IntegerSumMetricDataPoint extends SumMetricDataPoint {

  private final Integer value;

  public IntegerSumMetricDataPoint(
          @JsonProperty("timeUnixNano") final String timeUnixNano,
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
