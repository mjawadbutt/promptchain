package com.promptwise.promptchain.common.util.mapstruct;

import com.promptwise.promptchain.common.util.datetime.DateTimeConversionsUtil;

import javax.xml.datatype.XMLGregorianCalendar;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZonedDateTime;

public class DateTimeMapStructMapper {

  public DateTimeMapStructMapper() {
  }

  public ZonedDateTime xmlGregorianCalendarToZonedDateTime(XMLGregorianCalendar xmlGregorianCalendar) {
    return DateTimeConversionsUtil.xmlGregorianCalendarToZonedDateTime(xmlGregorianCalendar);
  }

  public XMLGregorianCalendar zonedDateTimeToXMLGregorianCalendar(ZonedDateTime zonedDateTime) {
    return DateTimeConversionsUtil.zonedDateTimeToXMLGregorianCalendar(zonedDateTime);
  }

  public Long zonedDateTimeToEpochMilliseconds(ZonedDateTime zonedDateTime) {
    return DateTimeConversionsUtil.zonedDateTimeToEpochMilliseconds(zonedDateTime);
  }

  public ZonedDateTime epochMillisecondsToZonedDateTime(Long milliSecondsSinceEpoch) {
    return DateTimeConversionsUtil.epochMillisecondsToZonedDateTime(milliSecondsSinceEpoch);
  }

  public Date localDateToSqlDate(LocalDate localDate) {
    return DateTimeConversionsUtil.localDateToSqlDate(localDate);
  }

  public LocalDate sqlDateToLocalDate(Date sqlDate) {
    return DateTimeConversionsUtil.sqlDateToLocalDate(sqlDate);
  }

}
