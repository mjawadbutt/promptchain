package com.promptwise.promptchain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MetricDetail<T extends MetricDataPoint> {

  private final List<T> dataPoints;

  public MetricDetail(final List<T> dataPoints) {
    this.dataPoints = dataPoints == null ? List.of() : List.copyOf(dataPoints);
  }

  public List<T> getDataPoints() {
    return dataPoints;
  }

}
