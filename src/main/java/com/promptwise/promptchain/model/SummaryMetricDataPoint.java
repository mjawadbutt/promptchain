package com.promptwise.promptchain.model;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;

public class SummaryMetricDataPoint extends MetricDataPoint {

  private final Integer count;
  private final Integer sum;
  private final SortedSet<HistogramQuantile> quantiles;

  public SummaryMetricDataPoint(final String timeUnixNano, final String startTimeUnixNano, final Integer count,
                                final Integer sum, final Set<HistogramQuantile> quantiles,
                                final List<SignalAttribute> attributes) {
    super(timeUnixNano, startTimeUnixNano, attributes);
    this.count = count;
    this.sum = sum;
    this.quantiles = quantiles == null
            ? java.util.Collections.emptySortedSet()
            : java.util.Collections.unmodifiableSortedSet(new java.util.TreeSet<>(quantiles));
  }

  public Integer getCount() {
    return count;
  }

  public Integer getSum() {
    return sum;
  }

  public SortedSet<HistogramQuantile> getQuantiles() {
    return quantiles;
  }

}
