package com.promptwise.promptchain.model;

import java.util.List;

public abstract class SumMetricDataPoint extends NumberMetricDataPoint {

  public SumMetricDataPoint(
          final String timeUnixNano,
          final String startTimeUnixNano,
          final List<SignalAttribute> attributes) {
    super(timeUnixNano, startTimeUnixNano, attributes);
  }

}
