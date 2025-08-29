package com.promptwise.promptchain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ScopeMetrics extends ScopeSignal<Metric<? extends MetricDetail<? extends MetricDataPoint>>> {

  public ScopeMetrics(@JsonProperty("scope") final ScopeSignalId scope,
                      @JsonProperty("metrics") final List<Metric<? extends MetricDetail<? extends MetricDataPoint>>> metrics) {
    super(scope, metrics);
  }

  @JsonProperty("metrics")
  public List<Metric<? extends MetricDetail<? extends MetricDataPoint>>> getMetrics() {
    return getSignals();
  }

}
