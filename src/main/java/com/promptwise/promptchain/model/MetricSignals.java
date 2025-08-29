package com.promptwise.promptchain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MetricSignals {

  private final List<MetricSignal> metricSignals;

  public MetricSignals(@JsonProperty("resourceMetrics") final List<MetricSignal> metricSignals) {
    this.metricSignals = metricSignals == null ? List.of() : List.copyOf(metricSignals);
  }

  @JsonProperty("resourceMetrics")
  public List<MetricSignal> getMetricSignals() {
    return metricSignals;
  }

}
