package com.promptwise.promptchain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TraceSignals {

  private final List<TraceSignal> traceSignals;

  public TraceSignals(@JsonProperty("resourceSpans") final List<TraceSignal> traceSignals) {
    this.traceSignals = traceSignals == null ? List.of() : List.copyOf(traceSignals);
  }

  @JsonProperty("resourceSpans")
  public List<TraceSignal> getTraceSignals() {
    return traceSignals;
  }

}
