package com.promptwise.promptchain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.promptwise.promptchain.common.util.json.JacksonUtil;

import java.util.List;

public class TraceSpanEvent {

  private final String timeUnixNano;
  private final String name;
  private final List<SignalAttribute> attributes;

  public TraceSpanEvent(
          @JsonProperty("timeUnixNano") String timeUnixNano,
          @JsonProperty("name") String name,
          @JsonProperty("attributes") List<SignalAttribute> attributes) {
    this.timeUnixNano = timeUnixNano;
    this.name = name;
    this.attributes = attributes;
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
