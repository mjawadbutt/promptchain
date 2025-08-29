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

//TODO-Jawad-now: add fix for Z->UTC
public class DateTimeConversionsUtil {
  private DateTimeConversionsUtil() {
  }

  public static ZonedDateTime xmlGregorianCalendarToZonedDateTime(final @NotNull XMLGregorianCalendar xmlGregorianCalendar) {
    Assert.notNull(xmlGregorianCalendar, "The 'xmlGregorianCalendar' cannot be 'null'!");
    return xmlGregorianCalendar.toGregorianCalendar().toZonedDateTime();
  }

  public static XMLGregorianCalendar zonedDateTimeToXMLGregorianCalendar(final @NotNull ZonedDateTime zonedDateTime) {
    Assert.notNull(zonedDateTime, "The 'zonedDateTime' cannot be 'null'!");
    GregorianCalendar gregorianCalendar = GregorianCalendar.from(zonedDateTime);

    try {
      return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
    } catch (DatatypeConfigurationException var3) {
      DatatypeConfigurationException dce = var3;
      throw CommonLibSystemException.create("An exception occurred while converting GregorianCalendar to XMLGregorianCalendar\nduring ZonedDateTime to XMLGregorianCalendar conversion! Please see cause for details.\n", dce);
    }
  }

  public static Long zonedDateTimeToEpochMilliseconds(final @NotNull ZonedDateTime zonedDateTime) {
    Assert.notNull(zonedDateTime, "The 'zonedDateTime' cannot be 'null'!");
    Instant instant = zonedDateTime.toInstant();
    Long milliSecondsSinceEpoch = instant.toEpochMilli();
    return milliSecondsSinceEpoch;
  }

  public static ZonedDateTime epochMillisecondsToZonedDateTime(final @NotNull Long milliSecondsSinceEpoch) {
    Assert.notNull(milliSecondsSinceEpoch, "The 'milliSecondsSinceEpoch' cannot be 'null'!");
    Instant instant = Instant.ofEpochMilli(milliSecondsSinceEpoch);
    return ZonedDateTime.ofInstant(instant, ZoneId.of("UTC"));
  }

  public static Date localDateToDate(final @NotNull LocalDate localDate) {
    Assert.notNull(localDate, "The 'localDate' cannot be 'null'!");
    return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
  }

  public static Date localDateTimeToDate(final @NotNull LocalDateTime localDateTime) {
    Assert.notNull(localDateTime, "The 'localDateTime' cannot be 'null'!");
    return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
  }

  public static LocalDate dateToLocalDate(final @NotNull Date date) {
    Assert.notNull(date, "The 'date' cannot be 'null'!");
    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
  }

  public static LocalDateTime dateToLocalDateTime(final @NotNull Date date) {
    Assert.notNull(date, "The 'date' cannot be 'null'!");
    return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
  }

  public static java.sql.Date localDateToSqlDate(final @NotNull LocalDate localDate) {
    Assert.notNull(localDate, "The 'localDate' cannot be 'null'!");
    return java.sql.Date.valueOf(localDate);
  }

  public static LocalDate sqlDateToLocalDate(@NotNull final java.sql.@NotNull Date sqlDate) {
    Assert.notNull(sqlDate, "The 'sqlDate' cannot be 'null'!");
    return sqlDate.toLocalDate();
  }
}
