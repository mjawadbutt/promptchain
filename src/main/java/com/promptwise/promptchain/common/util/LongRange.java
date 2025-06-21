package com.promptwise.promptchain.common.util;

import org.springframework.util.Assert;

import java.util.Comparator;
import java.util.Objects;

//TODO: Can delete this and replace usages with apache lang3 version of LongRange.
public class LongRange implements Comparable<LongRange> {

  private static final Comparator<LongRange> NATURAL_ORDER_COMPARATOR =
          Comparator.comparing(LongRange::getStart).thenComparing(LongRange::getEnd);

  private final long start;
  private final long end;
  private final int hashCode;
  private final String toStringVal;

  public LongRange(long start, long end) {
    Assert.isTrue(start <= end, "'start' must be less than 'end'");
    this.start = start;
    this.end = end;
    hashCode = Objects.hash(getStart(), getEnd());
    toStringVal = String.format("{start: %d, end %d", start, end);
  }

  public final long getStart() {
    return start;
  }

  public final long getEnd() {
    return end;
  }

  public long rangeSize() {
    return getEnd() - getStart() + 1;
  }

  @Override
  public int compareTo(LongRange o) {
    return NATURAL_ORDER_COMPARATOR.compare(this, o);
  }

  @Override
  public boolean equals(final Object thatObj) {
    boolean isEqual;
    if (this == thatObj) {
      isEqual = true;
    } else if (thatObj instanceof LongRange) {
      return compareTo((LongRange) thatObj) == 0;
    } else {
      isEqual = false;
    }
    return isEqual;
  }

  @Override
  public int hashCode() {
    return hashCode;
  }

  @Override
  public String toString() {
    return toStringVal;
  }

}
