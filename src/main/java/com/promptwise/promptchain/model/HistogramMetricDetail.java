package com.promptwise.promptchain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class HistogramMetricDetail extends MetricDetail<HistogramMetricDataPoint> {

  private final String aggregationTemporality;

  public HistogramMetricDetail(
          @JsonProperty("dataPoints") final List<HistogramMetricDataPoint> histogramMetricDataPoints,
          final String aggregationTemporality) {
    super(histogramMetricDataPoints);
    this.aggregationTemporality = aggregationTemporality;
  }

  public String getAggregationTemporality() {
    return aggregationTemporality;
  }

  @JsonProperty("dataPoints")
  //-- For serialization. TODO: do we put here or just in base class enough? remove from one side.
  public List<HistogramMetricDataPoint> getHistogramMetricDataPoints() {
    return super.getDataPoints();
  }

}
