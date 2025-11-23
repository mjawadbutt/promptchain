package com.promptwise.promptchain.model;

import com.promptwise.promptchain.common.util.json.JacksonUtil;

import java.util.Comparator;
import java.util.Objects;

public class SummaryQuantile implements Comparable<SummaryQuantile> {

  private static final Comparator<SummaryQuantile> NATURAL_ORDER_COMPARATOR =
          Comparator.comparing(SummaryQuantile::getQuantile)
                  .thenComparing(SummaryQuantile::getValue);

  private final Double quantile;
  private final Double value;

  public SummaryQuantile(Double quantile, Double value) {
    this.quantile = quantile;
    this.value = value;
  }

  public Double getQuantile() {
    return quantile;
  }

  public Double getValue() {
    return value;
  }

  @Override
  public int compareTo(final SummaryQuantile other) {
    return NATURAL_ORDER_COMPARATOR.compare(this, other);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof final SummaryQuantile other)) return false;
    // equality bound to compareTo == 0
    return this.compareTo(other) == 0;
  }

  @Override
  public int hashCode() {
    // must be consistent with equals — only use quantile
    return Objects.hash(quantile, value);
  }

  @Override
  public String toString() {
    return JacksonUtil.getInstance().serializeObjectToJsonString(this);
  }

}
