package com.promptwise.promptchain.model;

import com.promptwise.promptchain.common.util.json.JacksonUtil;

import java.util.Objects;

public class SummaryQuantile implements Comparable<SummaryQuantile> {

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
  public int compareTo(SummaryQuantile other) {
    if (other == null) return 1; // non-null is greater than null
    return this.quantile.compareTo(other.quantile);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof SummaryQuantile)) return false;
    SummaryQuantile other = (SummaryQuantile) o;
    // equality bound to compareTo == 0
    return this.compareTo(other) == 0;
  }

  @Override
  public int hashCode() {
    // must be consistent with equals — only use quantile
    return Objects.hash(quantile);
  }

  @Override
  public String toString() {
    return JacksonUtil.getInstance().serializeObjectToJsonString(this);
  }

}
