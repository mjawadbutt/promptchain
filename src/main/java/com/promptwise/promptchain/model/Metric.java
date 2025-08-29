package com.promptwise.promptchain.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = GaugeMetric.class, name = "GAUGE"),
        @JsonSubTypes.Type(value = SumMetric.class, name = "SUM"),
        @JsonSubTypes.Type(value = HistogramMetric.class, name = "HISTOGRAM"),
        @JsonSubTypes.Type(value = SummaryMetric.class, name = "SUMMARY")
})
public class Metric<MD extends MetricDetail<? extends MetricDataPoint>> {
  private final String name;
  private final String unit;
  //TODO: Change to enum GAUGE, SUM, HISTOGRAM, SUMMARY.
  private final String type;
  private final MD metricDetail;

  protected Metric(final String name, final String type, final String unit, MD metricDetail) {
    this.name = name;
    this.type = type;
    this.unit = unit;
    this.metricDetail = metricDetail;
  }

  public String getName() {
    return name;
  }

  public String getUnit() {
    return unit;
  }

  public String getType() {
    return type;
  }

  public MD getMetricDetail() {
    return metricDetail;
  }

}
