package com.promptwise.promptchain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public final class MetricSignal extends TelemetrySignal<ScopeMetrics> {

  public MetricSignal(
          @JsonProperty("resource")
          @NotNull final SignalResource signalResource,
          @JsonProperty("scopeMetrics")
          @NotNull final List<ScopeMetrics> scopeMetrics) {
    super(signalResource, scopeMetrics);
  }

  @JsonProperty("scopeMetrics")
  public List<ScopeMetrics> getScopeSignals() {
    return super.getScopeSignals();
  }

}
