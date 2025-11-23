package com.promptwise.promptchain.model;

import com.promptwise.promptchain.common.util.json.JacksonUtil;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Objects;

//TODO: add @JsonProperty with standard oltp names (and optionally ordering for roundtrip json string comparison test)
//TODO: to the whole hierarchy
//TODO: Ensure all properties explicitly use @JsonProperty in constructor (deser) and getter (ser)
//TODO: Determine which fields are optional and which are required as per OLTP spec and code accordingly (i.e.
//TODO: @NotNull, Objects.requireNotNull(), Optional etc)
//TODO: Implement hashcode, equals, and compareTo as per OLTP specs
public class TelemetrySignal<S extends ScopeSignal<?>> {

  private final SignalResource signalResource;
  private final List<S> scopeSignals;

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
