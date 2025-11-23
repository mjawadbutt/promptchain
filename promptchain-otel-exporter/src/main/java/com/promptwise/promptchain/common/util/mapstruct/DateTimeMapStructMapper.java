package com.promptwise.promptchain.common.util.mapstruct;

import com.promptwise.promptchain.common.util.datetime.DateTimeConversionsUtil;

import javax.xml.datatype.XMLGregorianCalendar;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZonedDateTime;

public class DateTimeMapStructMapper {

  public DateTimeMapStructMapper() {
  }

  public ZonedDateTime xmlGregorianCalendarToUtcZonedDateTime(XMLGregorianCalendar xmlGregorianCalendar) {
    ZonedDateTime utcZonedDateTime = DateTimeConversionsUtil.xmlGregorianCalendarToUtcZonedDateTime(xmlGregorianCalendar);
    return utcZonedDateTime;
  }

  public XMLGregorianCalendar zonedDateTimeToUtcXmlGregorianCalendar(ZonedDateTime zonedDateTime) {
    return DateTimeConversionsUtil.zonedDateTimeToUtcXmlGregorianCalendar(zonedDateTime);
  }

  public Long zonedDateTimeToEpochMilliseconds(ZonedDateTime zonedDateTime) {
    return DateTimeConversionsUtil.zonedDateTimeToEpochMilliseconds(zonedDateTime);
  }

  public ZonedDateTime epochMillisecondsToUtcZonedDateTime(Long milliSecondsSinceEpoch) {
    ZonedDateTime utcZonedDateTime = DateTimeConversionsUtil.epochMillisecondsToZonedDateTime(milliSecondsSinceEpoch);
    return utcZonedDateTime;
  }

  public Date localDateToSqlDate(LocalDate localDate) {
    return DateTimeConversionsUtil.localDateToSqlDate(localDate);
  }

  public LocalDate sqlDateToLocalDate(Date sqlDate) {
    return DateTimeConversionsUtil.sqlDateToLocalDate(sqlDate);
  }

}
