package com.promptwise.promptchain.model;

import java.util.List;

public class HistogramMetricDataPoint extends MetricDataPoint {

  private final Long count;
  private final Double sum;
  private final List<Long> bucketCounts;
  private final List<Double> explicitBounds;

  public HistogramMetricDataPoint(
          final String timeUnixNano, final String startTimeUnixNano, final Long count, Double sum,
          final List<Long> bucketCounts, final List<Double> explicitBounds, final List<SignalAttribute> attributes) {
    super(timeUnixNano, startTimeUnixNano, attributes);
    this.count = count;
    this.sum = sum;
    this.bucketCounts = bucketCounts == null ? List.of() : List.copyOf(bucketCounts);
    this.explicitBounds = explicitBounds == null ? List.of() : List.copyOf(explicitBounds);
  }

  public Long getCount() {
    return count;
  }

  public Double getSum() {
    return sum;
  }

  public List<Long> getBucketCounts() {
    return bucketCounts;
  }

  public List<Double> getExplicitBounds() {
    return explicitBounds;
  }

}
