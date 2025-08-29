package com.promptwise.promptchain.model;

import com.promptwise.promptchain.common.util.json.JacksonUtil;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Objects;

public class TelemetrySignal<S extends ScopeSignal<?>> {

  private final SignalResource signalResource;
  private final List<S> scopeSignals;

  //TODO-now: add @JsonProperty with standard oltp names to the whole hierarchy
  public TelemetrySignal(@NotNull final SignalResource signalResource, @NotNull final List<S> scopeSignals) {
    this.signalResource = Objects.requireNonNull(signalResource, "signalResource must not be null");
    this.scopeSignals = scopeSignals == null ? List.of() : List.copyOf(scopeSignals);
  }

  public SignalResource getSignalResource() {
    return signalResource;
  }

  public List<S> getScopeSignals() {
    return scopeSignals;
  }

  @Override
  public int hashCode() {
    return Objects.hash(signalResource, scopeSignals);
  }

  @Override
  public String toString() {
    return JacksonUtil.getInstance().serializeObjectToJsonString(this);
  }
}
