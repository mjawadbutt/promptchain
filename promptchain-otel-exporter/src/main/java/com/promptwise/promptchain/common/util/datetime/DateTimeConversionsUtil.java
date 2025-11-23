package com.promptwise.promptchain.common.util.datetime;

import com.promptwise.promptchain.common.exception.CommonLibSystemException;
import jakarta.validation.constraints.NotNull;
import org.springframework.util.Assert;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateTimeConversionsUtil {
  private DateTimeConversionsUtil() {
  }

  /**
   * Considers the XMLGregorianCalendar to be a UTC date-time if no offset is present.
   */
  public static ZonedDateTime xmlGregorianCalendarToUtcZonedDateTime(
          @NotNull final XMLGregorianCalendar xmlGregorianCalendar) {
    Assert.notNull(xmlGregorianCalendar, "The 'xmlGregorianCalendar' cannot be 'null'!");
    return xmlGregorianCalendar.toGregorianCalendar(TimeZone.getTimeZone("Z"), null, null)
            .toZonedDateTime();
  }

  public static ZonedDateTime xmlGregorianCalendarToZonedDateTime(
          @NotNull final XMLGregorianCalendar xmlGregorianCalendar, TimeZone defaultTimeZoneIfNoOffset) {
    Assert.notNull(xmlGregorianCalendar, "The 'xmlGregorianCalendar' cannot be 'null'!");
    Assert.notNull(defaultTimeZoneIfNoOffset, "The 'defaultTimeZoneIfNoOffset' cannot be 'null'!");
    return xmlGregorianCalendar.toGregorianCalendar(defaultTimeZoneIfNoOffset, null, null)
            .toZonedDateTime();
  }

  public static XMLGregorianCalendar zonedDateTimeToUtcXmlGregorianCalendar(
          @NotNull final ZonedDateTime zonedDateTime) {
    Assert.notNull(zonedDateTime, "The 'zonedDateTime' cannot be 'null'!");
    //-- Convert to UTC timezone
    ZonedDateTime utcZonedDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("Z"));
    GregorianCalendar gregorianCalendar = GregorianCalendar.from(utcZonedDateTime);

    try {
      return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
    } catch (DatatypeConfigurationException dce) {
      throw CommonLibSystemException.create("""
              An exception occurred while converting GregorianCalendar to XMLGregorianCalendar 
              during ZonedDateTime to XMLGregorianCalendar conversion! Please see cause for details.""", dce);
    }
  }

  public static Long zonedDateTimeToEpochMilliseconds(@NotNull final ZonedDateTime zonedDateTime) {
    Assert.notNull(zonedDateTime, "The 'zonedDateTime' cannot be 'null'!");
    Instant instant = zonedDateTime.toInstant();
    Long milliSecondsSinceEpoch = instant.toEpochMilli();
    return milliSecondsSinceEpoch;
  }

  public static ZonedDateTime epochMillisecondsToZonedDateTime(@NotNull final Long milliSecondsSinceEpoch) {
    Assert.notNull(milliSecondsSinceEpoch, "The 'milliSecondsSinceEpoch' cannot be 'null'!");
    Instant instant = Instant.ofEpochMilli(milliSecondsSinceEpoch);
    return ZonedDateTime.ofInstant(instant, ZoneId.of("Z"));
  }

  public static Date localDateToDate(@NotNull final LocalDate localDate) {
    Assert.notNull(localDate, "The 'localDate' cannot be 'null'!");
    return Date.from(localDate.atStartOfDay(ZoneId.of("Z")).toInstant());
  }

  public static Date localDateTimeToDate(final @NotNull LocalDateTime localDateTime) {
    Assert.notNull(localDateTime, "The 'localDateTime' cannot be 'null'!");
    return Date.from(localDateTime.atZone(ZoneId.of("Z")).toInstant());
  }

  public static LocalDate dateToLocalDate(@NotNull final Date date) {
    Assert.notNull(date, "The 'date' cannot be 'null'!");
    return date.toInstant().atZone(ZoneId.of("Z")).toLocalDate();
  }

  public static LocalDateTime dateToLocalDateTime(@NotNull final Date date) {
    Assert.notNull(date, "The 'date' cannot be 'null'!");
    return LocalDateTime.ofInstant(date.toInstant(), ZoneId.of("Z"));
  }

  public static java.sql.Date localDateToSqlDate(@NotNull final LocalDate localDate) {
    Assert.notNull(localDate, "The 'localDate' cannot be 'null'!");
    return java.sql.Date.valueOf(localDate);
  }

  public static LocalDate sqlDateToLocalDate(@NotNull final java.sql.Date sqlDate) {
    Assert.notNull(sqlDate, "The 'sqlDate' cannot be 'null'!");
    return sqlDate.toLocalDate();
  }

  private ZonedDateTime normalizeToUtc(ZonedDateTime zonedDateTime) {
    return zonedDateTime != null ? zonedDateTime.withZoneSameInstant(ZoneId.of("Z")) : null;
  }

}
