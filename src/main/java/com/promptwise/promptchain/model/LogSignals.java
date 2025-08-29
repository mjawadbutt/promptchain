package com.promptwise.promptchain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class LogSignals {

  private final List<LogSignal> logSignals;

  public LogSignals(@JsonProperty("resourceLogs") final List<LogSignal> logSignals) {
    this.logSignals = logSignals == null ? List.of() : List.copyOf(logSignals);
  }

  @JsonProperty("resourceLogs")
  public List<LogSignal> getLogSignals() {
    return logSignals;
  }

}
