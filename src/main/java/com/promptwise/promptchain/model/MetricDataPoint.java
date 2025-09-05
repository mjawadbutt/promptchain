package com.promptwise.promptchain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Objects;

public class MetricDataPoint {
  private final String timeUnixNano;
  private final String startTimeUnixNano;
  private final List<SignalAttribute> attributes;

  MetricDataPoint(@JsonProperty("timeUnixNano")
                  @NotNull final String timeUnixNano,
                  final String startTimeUnixNano,
                  final List<SignalAttribute> attributes) {
    this.timeUnixNano = Objects.requireNonNull(timeUnixNano, "timeUnixNano must not be null");
    this.startTimeUnixNano = startTimeUnixNano;
    this.attributes = attributes == null ? List.of() : List.copyOf(attributes);
  }

  @JsonIgnore
  public Long getTimeUnixNanoAsLong() {
    return Long.parseUnsignedLong(timeUnixNano);
  }

  @JsonProperty("timeUnixNano")
  public final String getTimeUnixNano() {
    return timeUnixNano;
  }

  public final String getStartTimeUnixNano() {
    return startTimeUnixNano;
  }

  public final List<SignalAttribute> getAttributes() {
    return attributes;
  }
}
