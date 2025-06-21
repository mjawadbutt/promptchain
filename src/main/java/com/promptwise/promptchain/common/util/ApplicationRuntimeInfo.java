package com.promptwise.promptchain.common.util;

import com.promptwise.promptchain.common.util.json.JacksonUtil;
import com.promptwise.promptchain.common.util.system.JvmOperatingSystemInfo;
import jakarta.validation.constraints.NotNull;

import javax.imageio.ImageIO;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ApplicationRuntimeInfo {

  private final JvmOperatingSystemInfo jvmOperatingSystemInfo;
  //TODO: This make it tied to the "profile" concept. These should be in a 'customProperties' map.
  private final Set<String> activeProfiles = new HashSet<>();
  private final Set<String> serviceProviderSpecificNamesOfSupportedImageFormats = new HashSet<>();

  private ApplicationRuntimeInfo(JvmOperatingSystemInfo jvmOperatingSystemInfo, Set<String> activeProfiles,
                                 String[] serviceProviderSpecificNamesOfSupportedImageFormats) {
    this.jvmOperatingSystemInfo = jvmOperatingSystemInfo;
    if (activeProfiles != null) {
      this.activeProfiles.addAll(activeProfiles);
    }
    if (serviceProviderSpecificNamesOfSupportedImageFormats != null) {
      this.serviceProviderSpecificNamesOfSupportedImageFormats.addAll(Set.of(serviceProviderSpecificNamesOfSupportedImageFormats));
    }
  }

  public static ApplicationRuntimeInfo create(@NotNull final Set<String> activeProfiles) {
    String[] serviceProviderSpecificNamesOfSupportedImageFormats = ImageIO.getWriterFormatNames();
    return new ApplicationRuntimeInfo(JvmOperatingSystemInfo.INSTANCE, activeProfiles, serviceProviderSpecificNamesOfSupportedImageFormats);
  }

  public JvmOperatingSystemInfo getJvmOperatingSystemInfo() {
    return jvmOperatingSystemInfo;
  }

  public Set<String> getActiveProfiles() {
    return Collections.unmodifiableSet(activeProfiles);
  }

  public Set<String> getServiceProviderSpecificNamesOfSupportedImageFormats() {
    return Collections.unmodifiableSet(serviceProviderSpecificNamesOfSupportedImageFormats);
  }

  @Override
  public String toString() {
    return JacksonUtil.getInstance().serializeObjectToJsonString(this);
  }

}
