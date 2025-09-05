package com.promptwise.promptchain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = GaugeMetric.class, name = "gauge"),
        @JsonSubTypes.Type(value = SumMetric.class, name = "sum"),
        @JsonSubTypes.Type(value = HistogramMetric.class, name = "histogram"),
        @JsonSubTypes.Type(value = SummaryMetric.class, name = "summary")
})
public class Metric<MD extends MetricDetail<? extends MetricDataPoint>> {
  private final String name;
  private final String unit;
  private final MetricType type;
  private final MD metricDetail;

  protected Metric(final String name, @JsonProperty("type") @NotNull final MetricType type,
                   final String unit, MD metricDetail) {
    this.name = name;
    this.type = Objects.requireNonNull(type, "The parameter 'type' cannot be 'null'!");
    this.unit = unit;
    this.metricDetail = metricDetail;
  }

  public String getName() {
    return name;
  }

  public MetricType getType() {
    return type;
  }

  public String getUnit() {
    return unit;
  }

  public MD getMetricDetail() {
    return metricDetail;
  }

}
