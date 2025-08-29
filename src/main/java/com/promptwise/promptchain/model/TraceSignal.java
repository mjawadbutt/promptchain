package com.promptwise.promptchain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public final class TraceSignal extends TelemetrySignal<ScopeTraces> {

  public TraceSignal(
          @JsonProperty("resource")
          @NotNull final SignalResource signalResource,
          @JsonProperty("scopeSpans")
          @NotNull final List<ScopeTraces> scopeTraces) {
    super(signalResource, scopeTraces);
  }

}