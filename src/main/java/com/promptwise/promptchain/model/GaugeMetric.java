package com.promptwise.promptchain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class GaugeMetric extends Metric<GaugeMetricDetail> {

  public GaugeMetric(final String name, final String type, final String unit,
                     @JsonProperty("gauge") GaugeMetricDetail gaugeMetricDetail) {
    super(name, type, unit, gaugeMetricDetail);
  }

  @JsonProperty("gauge")
  @Override
  public GaugeMetricDetail getMetricDetail() {
    return super.getMetricDetail();
  }

}
