package com.promptwise.promptchain.model;

import com.promptwise.promptchain.common.util.json.JacksonUtil;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Objects;

public class ScopeSignal<U> {

  private final ScopeSignalId scope;
  private final List<U> signals;

  public ScopeSignal(@NotNull final ScopeSignalId scopeSignalId, @NotNull final List<U> signals) {
    this.scope = Objects.requireNonNull(scopeSignalId, "scopeSignalId must not be null");
    this.signals = signals == null ? List.of() : List.copyOf(signals);
  }

  public ScopeSignalId getScope() {
    return scope;
  }

  public List<U> getSignals() {
    return signals;
  }

  @Override
  public int hashCode() {
    return Objects.hash(scope, signals);
  }

  @Override
  public String toString() {
    return JacksonUtil.getInstance().serializeObjectToJsonString(this);
  }
}
