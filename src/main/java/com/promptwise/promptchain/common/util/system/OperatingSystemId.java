package com.promptwise.promptchain.common.util.system;

import com.promptwise.promptchain.common.util.EnumHelper;

public enum OperatingSystemId {

  AIX("AIX"),
  HP_UX("HP-UX"),
  OS_400("OS/400"),
  IRIX("Irix"),
  LINUX("Linux"),
  MAC("Mac"),
  MAC_OSX("Mac OS X"),
  FREE_BSD("FreeBSD"),
  OPEN_BSD("OpenBSD"),
  NET_BSD("NetBSD"),
  OS2("OS/2"),
  SOLARIS("Solaris"),
  SUN_OS("SunOS"),
  ZOS("z/OS"),
  WINDOWS("Windows"),
  WINDOWS_2000("Windows 2000"),
  WINDOWS_2003("Windows 2003"),
  WINDOWS_SERVER_2008("Windows Server 2008"),
  WINDOWS_SERVER_2012("Windows Server 2012"),
  WINDOWS_95("Windows 95"),
  WINDOWS_98("Windows 98"),
  WINDOWS_ME("Windows Me"),
  WINDOWS_NT("Windows NT"),
  WINDOWS_XP("Windows XP"),
  WINDOWS_VISTA("Windows Vista"),
  WINDOWS_7("Windows 7"),
  WINDOWS_8("Windows 8"),
  WINDOWS_10("Windows 10"),
  WINDOWS_11("Windows 11"),
  UNKNOWN("Unknown");

  private final String osName;

  OperatingSystemId(String osName) {
    this.osName = osName;
  }

  @EnumHelper.CustomValueGetterMethod
  public String getOsName() {
    return osName;
  }

}
