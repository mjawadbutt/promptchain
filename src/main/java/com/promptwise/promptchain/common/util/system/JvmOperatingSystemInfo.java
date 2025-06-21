package com.promptwise.promptchain.common.util.system;

import org.apache.commons.lang3.ArchUtils;
import org.apache.commons.lang3.SystemProperties;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.arch.Processor;

public class JvmOperatingSystemInfo {

  private final OperatingSystemId operatingSystemId;
  private final String osVersion;
  private final String osArch;
  private final Processor processor;

  public static final JvmOperatingSystemInfo INSTANCE = create();

  private JvmOperatingSystemInfo(OperatingSystemId operatingSystemId, String osVersion, String osArch, Processor processor) {
    this.operatingSystemId = operatingSystemId;
    this.osVersion = osVersion;
    this.osArch = osArch;
    this.processor = processor;
  }

  /**
   * If in future we need some info from the apps that use this, then we can change it from a singleton to a
   * normal factory method and let the callers keep it as a singleton.
   */
  private static JvmOperatingSystemInfo create() {
    OperatingSystemId operatingSystemId;
    if (SystemUtils.IS_OS_AIX) {
      operatingSystemId = OperatingSystemId.AIX;
    } else if (SystemUtils.IS_OS_HP_UX) {
      operatingSystemId = OperatingSystemId.HP_UX;
    } else if (SystemUtils.IS_OS_400) {
      operatingSystemId = OperatingSystemId.OS_400;
    } else if (SystemUtils.IS_OS_IRIX) {
      operatingSystemId = OperatingSystemId.IRIX;
    } else if (SystemUtils.IS_OS_LINUX) {
      operatingSystemId = OperatingSystemId.LINUX;
    } else if (SystemUtils.IS_OS_MAC) {
      operatingSystemId = OperatingSystemId.MAC;
    } else if (SystemUtils.IS_OS_MAC_OSX) {
      operatingSystemId = OperatingSystemId.MAC_OSX;
    } else if (SystemUtils.IS_OS_FREE_BSD) {
      operatingSystemId = OperatingSystemId.FREE_BSD;
    } else if (SystemUtils.IS_OS_OPEN_BSD) {
      operatingSystemId = OperatingSystemId.OPEN_BSD;
    } else if (SystemUtils.IS_OS_NET_BSD) {
      operatingSystemId = OperatingSystemId.NET_BSD;
    } else if (SystemUtils.IS_OS_OS2) {
      operatingSystemId = OperatingSystemId.OS2;
    } else if (SystemUtils.IS_OS_SOLARIS) {
      operatingSystemId = OperatingSystemId.SOLARIS;
    } else if (SystemUtils.IS_OS_SUN_OS) {
      operatingSystemId = OperatingSystemId.SUN_OS;
    } else if (SystemUtils.IS_OS_WINDOWS_2000) {
      operatingSystemId = OperatingSystemId.WINDOWS_2000;
    } else if (SystemUtils.IS_OS_WINDOWS_2003) {
      operatingSystemId = OperatingSystemId.WINDOWS_2003;
    } else if (SystemUtils.IS_OS_WINDOWS_2008) {
      operatingSystemId = OperatingSystemId.WINDOWS_SERVER_2008;
    } else if (SystemUtils.IS_OS_WINDOWS_2012) {
      operatingSystemId = OperatingSystemId.WINDOWS_SERVER_2012;
    } else if (SystemUtils.IS_OS_WINDOWS_95) {
      operatingSystemId = OperatingSystemId.WINDOWS_95;
    } else if (SystemUtils.IS_OS_WINDOWS_98) {
      operatingSystemId = OperatingSystemId.WINDOWS_98;
    } else if (SystemUtils.IS_OS_WINDOWS_ME) {
      operatingSystemId = OperatingSystemId.WINDOWS_ME;
    } else if (SystemUtils.IS_OS_WINDOWS_NT) {
      operatingSystemId = OperatingSystemId.WINDOWS_NT;
    } else if (SystemUtils.IS_OS_WINDOWS_XP) {
      operatingSystemId = OperatingSystemId.WINDOWS_XP;
    } else if (SystemUtils.IS_OS_WINDOWS_VISTA) {
      operatingSystemId = OperatingSystemId.WINDOWS_VISTA;
    } else if (SystemUtils.IS_OS_WINDOWS_7) {
      operatingSystemId = OperatingSystemId.WINDOWS_7;
    } else if (SystemUtils.IS_OS_WINDOWS_8) {
      operatingSystemId = OperatingSystemId.WINDOWS_8;
    } else if (SystemUtils.IS_OS_WINDOWS_10) {
      operatingSystemId = OperatingSystemId.WINDOWS_10;
    } else if (SystemUtils.IS_OS_WINDOWS_11) {
      operatingSystemId = OperatingSystemId.WINDOWS_11;
    } else if (SystemUtils.IS_OS_WINDOWS) {
      operatingSystemId = OperatingSystemId.WINDOWS;
    } else if (SystemUtils.IS_OS_ZOS) {
      operatingSystemId = OperatingSystemId.ZOS;
    } else {
      operatingSystemId = OperatingSystemId.UNKNOWN;
    }
    return new JvmOperatingSystemInfo(operatingSystemId, SystemProperties.getOsVersion(), SystemProperties.getOsArch(),
            ArchUtils.getProcessor());
  }

  public OperatingSystemId getOperatingSystemId() {
    return operatingSystemId;
  }

  public String getOsVersion() {
    return osVersion;
  }

  public String getOsArch() {
    return osArch;
  }

  public Processor getProcessor() {
    return processor;
  }

}
