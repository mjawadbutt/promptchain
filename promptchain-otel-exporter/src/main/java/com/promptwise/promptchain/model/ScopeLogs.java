package com.promptwise.promptchain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ScopeLogs extends ScopeSignal<LogRecord> {

  public ScopeLogs(@JsonProperty("scope") final ScopeSignalId scope,
                   @JsonProperty("logRecords") final List<LogRecord> logRecords) {
    super(scope, logRecords);
  }

  @JsonProperty("logRecords")
  public List<LogRecord> getTraces() {
    return getSignals();
  }

}
