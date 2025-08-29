package com.promptwise.promptchain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class GaugeMetricDetail extends MetricDetail<NumberMetricDataPoint> {

  private final List<NumberMetricDataPoint> numberMetricDataPoints;

  public GaugeMetricDetail(
          @JsonProperty("dataPoints")
          @JsonDeserialize(using = NumberMetricDataPointDeserializer.class)
          final List<NumberMetricDataPoint> numberMetricDataPoints) {
    super(numberMetricDataPoints);
    this.numberMetricDataPoints = numberMetricDataPoints == null ? List.of() : List.copyOf(numberMetricDataPoints);
  }

  @JsonProperty("dataPoints")
  public List<NumberMetricDataPoint> getNumberMetricDataPoints() {
    return numberMetricDataPoints;
  }

}
