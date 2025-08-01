package com.promptwise.promptchain.common.util.datetime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class DateTimeFormattingUtil {

  private static final String DP_ddMMyyyy_FS = "dd/MM/yyyy";
  private static final String DP_ddMMyy_FS = "dd/MM/yy";
  private static final String DP_yyyyMMdd_HY = "yyyy-MM-dd";
  private static final String DP_ddMMyyyy = "ddMMyyyy";
  private static final String DTP_HHmmddMMyyyy_COLON_SPACE_FS = "HH:mm dd/MM/yyyy";

  public static final DateTimeFormatter DTF_ddMMyyyy_FS_ENGLISH = DateTimeFormatter.ofPattern(DP_ddMMyyyy_FS, Locale.ENGLISH);
  public static final DateTimeFormatter DTF_ddMMyy_FS_ENGLISH = DateTimeFormatter.ofPattern(DP_ddMMyy_FS, Locale.ENGLISH);
  public static final DateTimeFormatter DTF_yyyyMMdd_HY_ENGLISH = DateTimeFormatter.ofPattern(DP_yyyyMMdd_HY, Locale.ENGLISH);
  public static final DateTimeFormatter DTF_ddMMyyyy_ENGLISH = DateTimeFormatter.ofPattern(DP_ddMMyyyy, Locale.ENGLISH);
  public static final DateTimeFormatter DTF_HHmmddMMyyyy_COLON_SPACE_FS = DateTimeFormatter.ofPattern(
          DTP_HHmmddMMyyyy_COLON_SPACE_FS, Locale.ENGLISH);

  public static Date parseDate(String strDate, DateTimeFormatter dateTimeFormatter) {
    LocalDate localDate = LocalDate.parse(strDate, dateTimeFormatter);
    return DateTimeConversionsUtil.localDateToDate(localDate);
  }

  public static Date parseDateTime(String strDateTime, DateTimeFormatter dateTimeFormatter) {
    LocalDateTime localDateTime = LocalDateTime.parse(strDateTime, dateTimeFormatter);
    return DateTimeConversionsUtil.localDateTimeToDate(localDateTime);
  }

  public static String formatDate(Date date, DateTimeFormatter dateTimeFormatter) {
    LocalDate localDate = DateTimeConversionsUtil.dateToLocalDate(date);
    return localDate.format(dateTimeFormatter);
  }

  public static String formatDateTime(Date date, DateTimeFormatter dateTimeFormatter) {
    LocalDateTime localDateTime = DateTimeConversionsUtil.dateToLocalDateTime(date);
    return localDateTime.format(dateTimeFormatter);
  }

}
