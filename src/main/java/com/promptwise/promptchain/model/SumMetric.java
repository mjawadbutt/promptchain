package com.promptwise.promptchain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class SumMetric extends Metric<SumMetricDetail> {

  public SumMetric(final String name, final String type, final String unit,
                   @JsonProperty("sum") SumMetricDetail sumMetricDetail) {
    super(name, type, unit, sumMetricDetail);
  }

  @JsonProperty("sum")
  @Override
  public SumMetricDetail getMetricDetail() {
    return super.getMetricDetail();
  }

}
