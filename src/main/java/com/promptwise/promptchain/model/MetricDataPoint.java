package com.promptwise.promptchain.model;

import java.util.List;

public class MetricDataPoint {
  private final String timeUnixNano;
  private final String startTimeUnixNano;
  private final List<SignalAttribute> attributes;

  MetricDataPoint(final String timeUnixNano, final String startTimeUnixNano,
                  final List<SignalAttribute> attributes) {
    this.timeUnixNano = timeUnixNano;
    this.startTimeUnixNano = startTimeUnixNano;
    this.attributes = attributes == null ? List.of() : List.copyOf(attributes);
  }

  public String getTimeUnixNano() {
    return timeUnixNano;
  }

  public String getStartTimeUnixNano() {
    return startTimeUnixNano;
  }

  public List<SignalAttribute> getAttributes() {
    return attributes;
  }
}
