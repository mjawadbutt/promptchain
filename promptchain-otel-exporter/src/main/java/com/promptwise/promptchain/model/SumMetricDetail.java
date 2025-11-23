package com.promptwise.promptchain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class SumMetricDetail extends MetricDetail<SumMetricDataPoint> {

  private final String aggregationTemporality;
  private final boolean isMonotonic;

  public SumMetricDetail(
          final String aggregationTemporality, @JsonProperty("isMonotonic") final boolean isMonotonic,
          @JsonProperty("dataPoints")
          @JsonDeserialize(using = SumMetricDataPointListDeserializer.class) final List<SumMetricDataPoint> sumMetricDataPoints) {
    super(sumMetricDataPoints);
    this.aggregationTemporality = aggregationTemporality;
    this.isMonotonic = isMonotonic;
  }

  public String getAggregationTemporality() {
    return aggregationTemporality;
  }

  @JsonProperty("isMonotonic")
  public boolean isMonotonic() {
    return isMonotonic;
  }

  @JsonProperty("dataPoints")
  public List<SumMetricDataPoint> getSumMetricDataPoints() {
    return super.getDataPoints();
  }

}
