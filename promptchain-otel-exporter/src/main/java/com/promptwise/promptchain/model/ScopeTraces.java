package com.promptwise.promptchain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ScopeTraces extends ScopeSignal<TraceSpan> {

  public ScopeTraces(@JsonProperty("scope") final ScopeSignalId scope,
                     @JsonProperty("spans") final List<TraceSpan> traceSpans) {
    super(scope, traceSpans);
  }

  @JsonProperty("spans")
  public List<TraceSpan> getTraces() {
    return getSignals();
  }

}
