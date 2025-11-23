package com.promptwise.promptchain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class HistogramMetricDetail extends MetricDetail<HistogramMetricDataPoint> {

  private final String aggregationTemporality;

  public HistogramMetricDetail(
          final String aggregationTemporality,
          @JsonProperty("dataPoints") final List<HistogramMetricDataPoint> histogramMetricDataPoints) {
    super(histogramMetricDataPoints);
    this.aggregationTemporality = aggregationTemporality;
  }

  public String getAggregationTemporality() {
    return aggregationTemporality;
  }

  //-- This is for serialization, and the one on constructor is for deserialization.
  @JsonProperty("dataPoints")
  public List<HistogramMetricDataPoint> getHistogramMetricDataPoints() {
    return super.getDataPoints();
  }

}
