package com.promptwise.promptchain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class HistogramMetric extends Metric<HistogramMetricDetail> {

  public HistogramMetric(final String name, final String type, final String unit,
                         @JsonProperty("histogram") HistogramMetricDetail histogramMetricDetail) {
    super(name, type, unit, histogramMetricDetail);
  }

}
