package com.promptwise.promptchain.model;

import java.util.List;

public abstract class NumberMetricDataPoint extends MetricDataPoint {

  public NumberMetricDataPoint(final String timeUnixNano, final String startTimeUnixNano,
                               final List<SignalAttribute> attributes) {
    super(timeUnixNano, startTimeUnixNano, attributes);
  }

  public abstract Number getValue();
}
