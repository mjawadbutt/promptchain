package com.promptwise.promptchain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SummaryMetricDetail extends MetricDetail<SummaryMetricDataPoint> {

  public SummaryMetricDetail(
          @JsonProperty("dataPoints") final List<SummaryMetricDataPoint> summaryMetricDataPoints) {
    super(summaryMetricDataPoints);
  }

  @JsonProperty("dataPoints")
  public List<SummaryMetricDataPoint> getSummaryMetricDataPoints() {
    return super.getDataPoints();
  }

}
