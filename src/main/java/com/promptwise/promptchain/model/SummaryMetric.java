package com.promptwise.promptchain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class SummaryMetric extends Metric<SummaryMetricDetail> {

  public SummaryMetric(final String name, final String type, final String unit,
                       @JsonProperty("summary") SummaryMetricDetail summaryMetricDetail) {
    super(name, type, unit, summaryMetricDetail);
  }

  @JsonProperty("summary")
  @Override
  public SummaryMetricDetail getMetricDetail() {
    return super.getMetricDetail();
  }

}