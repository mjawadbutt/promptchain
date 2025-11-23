package com.promptwise.promptchain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;

public class SummaryMetricDataPoint extends MetricDataPoint {

  private final Integer count;
  private final Double sum;
  private final SortedSet<SummaryQuantile> quantileValues;

  public SummaryMetricDataPoint(final String timeUnixNano, final String startTimeUnixNano, final Integer count,
                                final Double sum, @JsonProperty("quantileValues") final Set<SummaryQuantile> quantileValues,
                                final List<SignalAttribute> attributes) {
    super(timeUnixNano, startTimeUnixNano, attributes);
    this.count = count;
    this.sum = sum;
    this.quantileValues = quantileValues == null
            ? java.util.Collections.emptySortedSet()
            : java.util.Collections.unmodifiableSortedSet(new java.util.TreeSet<>(quantileValues));
  }

  public Integer getCount() {
    return count;
  }

  public Double getSum() {
    return sum;
  }

  @JsonProperty("quantileValues")
  public SortedSet<SummaryQuantile> getQuantileValues() {
    return quantileValues;
  }

}
