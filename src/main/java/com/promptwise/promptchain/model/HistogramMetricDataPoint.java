package com.promptwise.promptchain.model;

import java.util.List;

public class HistogramMetricDataPoint extends MetricDataPoint {

  private final Integer count;
  private final Double sum;
  private final List<Integer> bucketCounts;
  private final List<Integer> explicitBounds;

  public HistogramMetricDataPoint(
          final String timeUnixNano, final String startTimeUnixNano, final Integer count, Double sum,
          final List<Integer> bucketCounts, final List<Integer> explicitBounds, final List<SignalAttribute> attributes) {
    super(timeUnixNano, startTimeUnixNano, attributes);
    this.count = count;
    this.sum = sum;
    this.bucketCounts = bucketCounts == null ? List.of() : List.copyOf(bucketCounts);
    this.explicitBounds = explicitBounds == null ? List.of() : List.copyOf(explicitBounds);
  }

  public Integer getCount() {
    return count;
  }

  public Double getSum() {
    return sum;
  }

  public List<Integer> getBucketCounts() {
    return bucketCounts;
  }

  public List<Integer> getExplicitBounds() {
    return explicitBounds;
  }

}
