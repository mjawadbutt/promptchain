package com.promptwise.promptchain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class SumMetricDetail extends MetricDetail<SumMetricDataPoint> {

  private final String aggregationTemporality;
  private final boolean isMonotonic;

  public SumMetricDetail(
          @JsonProperty("dataPoints")
          @JsonDeserialize(using = SumMetricDataPointDeserializer.class) final List<SumMetricDataPoint> sumMetricDataPoints,
          final String aggregationTemporality, final boolean isMonotonic) {
    super(sumMetricDataPoints);
    this.aggregationTemporality = aggregationTemporality;
    this.isMonotonic = isMonotonic;
  }

  public String getAggregationTemporality() {
    return aggregationTemporality;
  }

  public boolean isMonotonic() {
    return isMonotonic;
  }

  @JsonProperty("dataPoints")
  public List<SumMetricDataPoint> getSumMetricDataPoints() {
    return super.getDataPoints();
  }

}
