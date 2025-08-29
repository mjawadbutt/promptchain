package com.promptwise.promptchain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public final class LogSignal extends TelemetrySignal<ScopeLogs> {

  public LogSignal(
          @JsonProperty("resource")
          @NotNull final SignalResource signalResource,
          @JsonProperty("scopeLogs")
          @NotNull final List<ScopeLogs> scopeLogs) {
    super(signalResource, scopeLogs);
  }

}