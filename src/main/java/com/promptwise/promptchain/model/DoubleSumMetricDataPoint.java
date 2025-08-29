package com.promptwise.promptchain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DoubleSumMetricDataPoint extends SumMetricDataPoint {

  private final Double value;

  public DoubleSumMetricDataPoint(
          @JsonProperty("timeUnixNano") final String timeUnixNano,
          @JsonProperty("startTimeUnixNano") final String startTimeUnixNano,
          @JsonProperty("attributes") final List<SignalAttribute> attributes,
          @JsonProperty("asDouble") final Double value) {
    super(timeUnixNano, startTimeUnixNano, attributes);
    this.value = value;
  }

  @JsonProperty("asDouble")
  public Double getValue() {
    return value;
  }

}
