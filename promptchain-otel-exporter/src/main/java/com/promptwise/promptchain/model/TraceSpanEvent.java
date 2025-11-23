package com.promptwise.promptchain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.promptwise.promptchain.common.util.json.JacksonUtil;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Objects;

public class TraceSpanEvent {

  private final String timeUnixNano;
  private final String name;
  private final List<SignalAttribute> attributes;

  public TraceSpanEvent(
          @JsonProperty("timeUnixNano") @NotNull final String timeUnixNano,
          @JsonProperty("name") final String name,
          @JsonProperty("attributes") final List<SignalAttribute> attributes) {
    this.timeUnixNano = Objects.requireNonNull(timeUnixNano, "timeUnixNano must not be null");
    this.name = name;
    this.attributes = attributes;
  }

  @JsonIgnore
  public Long getTimeUnixNanoAsLong() {
    return Long.parseUnsignedLong(timeUnixNano);
  }

  public String getTimeUnixNano() {
    return timeUnixNano;
  }

  public String getName() {
    return name;
  }

  public List<SignalAttribute> getAttributes() {
    return attributes;
  }

  @Override
  public String toString() {
    return JacksonUtil.getInstance().serializeObjectToJsonString(this);
  }
}
