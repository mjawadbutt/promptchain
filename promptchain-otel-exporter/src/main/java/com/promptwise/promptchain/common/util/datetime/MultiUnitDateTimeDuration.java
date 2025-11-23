package com.promptwise.promptchain.common.util.datetime;

import com.promptwise.promptchain.common.util.json.JacksonDeserializerForChronoUnit;
import com.promptwise.promptchain.common.util.json.JacksonSerializerForChronoUnit;
import com.promptwise.promptchain.common.util.json.JacksonUtil;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.CaseFormat;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Allows callers to create a multi-unit representation of a 'duration' between two date-times in terms of
 * defined ChronoUnits for display purpose only.
 * Note that instances of this class can only be serialized and cannot be de-serialized because the serialized version
 * loses context (i.e. the dates which were used to calculate the duration). However, this is by design and further
 * emphasizes the fact that the sole purpose of this class is to allow users to create a duration representation
 * that can only be used for display purpose only.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class MultiUnitDateTimeDuration {

  /**
   * The ordering IS A FUNCTIONAL REQUIREMENT. It is so that we go from the biggest unit to the
   * smallest one when accumulating duration unit values from a duration while creating an instance.
   */
  private static final SortedSet<ChronoUnit> ORDERED_DURATION_UNITS = Stream.of(ChronoUnit.values())
          .collect(Collectors.toCollection(() -> new TreeSet<ChronoUnit>(Comparator.reverseOrder())));

  /**
   * The ordering is a non-strict functional requirement as it is only to enable serialization in a more readable
   * format (high order durations before lower ones (i.e. year before month etc.).
   * In addition, it also enables ease of debugging.
   */
  private final SortedMap<ChronoUnit, Long> durationMap = new TreeMap<>(Comparator.reverseOrder());

  private MultiUnitDateTimeDuration() {
  }

  private MultiUnitDateTimeDuration(@NotEmpty final Map<ChronoUnit, Long> durationMap) {
    Assert.notEmpty(durationMap, "The parameter 'durationMap' cannot be empty!");
    for (Map.Entry<ChronoUnit, Long> entry : durationMap.entrySet()) {
      Assert.notNull(entry.getValue(), String.format("""
              The 'durationMap' must not contain any 'null' value(s)! The value for the unit '%s' is 'null'.
              """, entry.getKey()));
    }
    this.durationMap.putAll(durationMap);
  }

  public static MultiUnitDateTimeDuration create(final ZonedDateTime zonedDateTime1,
                                                 final ZonedDateTime zonedDateTime2,
                                                 final Set<ChronoUnit> durationUnitSet) {
    ZonedDateTime utcZonedDateTime1 = zonedDateTime1.withZoneSameInstant(ZoneOffset.UTC);
    ZonedDateTime utcZonedDateTime2 = zonedDateTime2.withZoneSameInstant(ZoneOffset.UTC);
    LocalDateTime localDateTime1 = utcZonedDateTime1.toLocalDateTime();
    LocalDateTime localDateTime2 = utcZonedDateTime2.toLocalDateTime();
    return MultiUnitDateTimeDuration.create(localDateTime1, localDateTime2, durationUnitSet);
  }

  public static MultiUnitDateTimeDuration create(final LocalDateTime localDateTime1,
                                                 final LocalDateTime localDateTime2,
                                                 final Set<ChronoUnit> durationUnitSet) {
    Map<ChronoUnit, Long> durationMap = createDurationMap(localDateTime1, localDateTime2, durationUnitSet);
    return MultiUnitDateTimeDuration.create(durationMap);
  }

  public static MultiUnitDateTimeDuration create(final Map<ChronoUnit, Long> durationMap) {
    return new MultiUnitDateTimeDuration(durationMap);
  }

  public static Map<ChronoUnit, Long> createDurationMap(final LocalDateTime localDateTime1,
                                                        LocalDateTime localDateTime2,
                                                        @NotEmpty final Set<ChronoUnit> durationUnitSet) {
    Assert.notEmpty(durationUnitSet, "The parameter 'durationUnitSet' cannot be empty!");
    Map<ChronoUnit, Long> durationMap = new HashMap<>();
    for (ChronoUnit durationUnit : ORDERED_DURATION_UNITS) {
      if (durationUnitSet.contains(durationUnit)) {
        long durationValue = durationUnit.between(localDateTime1, localDateTime2);
        localDateTime2 = localDateTime2.minus(durationValue, durationUnit);
        durationMap.put(durationUnit, durationValue);
      }
    }
    //TODO-MultiUnitDuration: Add support for remainder? can use the serialized version as a child node.
    return durationMap;
  }

  @JsonAnySetter
  @JsonDeserialize(using = JacksonDeserializerForChronoUnit.class)
  private void setDurationEntry(String lowerCamelCaseDurationUnitEnumName, Long durationValue) {
    String enumName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, lowerCamelCaseDurationUnitEnumName);
    ChronoUnit durationUnit = ChronoUnit.valueOf(enumName);
    durationMap.put(durationUnit, durationValue);
  }

  @JsonAnyGetter
  @JsonSerialize(keyUsing = JacksonSerializerForChronoUnit.class)
  public SortedMap<ChronoUnit, Long> getDurationMap() {
    return Collections.unmodifiableSortedMap(durationMap);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MultiUnitDateTimeDuration that = (MultiUnitDateTimeDuration) o;
    return Objects.equals(durationMap, that.durationMap);
  }

  @Override
  public int hashCode() {
    return Objects.hash(durationMap);
  }

  @Override
  public String toString() {
    return JacksonUtil.getInstance().serializeObjectToJsonString(this);
  }

}
