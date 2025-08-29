package com.promptwise.promptchain.model;

import com.promptwise.promptchain.common.util.json.JacksonUtil;

import java.util.Objects;

public class HistogramQuantile implements Comparable<HistogramQuantile> {

  private final Integer quantile;
  private final Integer value;

  public HistogramQuantile(Integer quantile, Integer value) {
    this.quantile = quantile;
    this.value = value;
  }

  public Integer getQuantile() {
    return quantile;
  }

  public Integer getValue() {
    return value;
  }

  @Override
  public int compareTo(HistogramQuantile other) {
    if (other == null) return 1; // non-null is greater than null
    return this.quantile.compareTo(other.quantile);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof HistogramQuantile)) return false;
    HistogramQuantile other = (HistogramQuantile) o;
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
