package com.promptwise.promptchain.common.util;

import com.promptwise.promptchain.common.util.json.JacksonUtil;
import com.fasterxml.jackson.core.Version;
import jakarta.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class ApplicationBuildInfo {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationBuildInfo.class);

  private final String applicationId;
  private final String applicationName;
  private final String description;
  private final String artifactVersionLabel;
  private final Version artifactVersion;
  private final String scmUrl;

  public ApplicationBuildInfo(final String applicationId,
                              final String applicationName,
                              final String description,
                              @Pattern(regexp = "^[0-9]\\.[0-9]\\.[-0-9](-SNAPSHOT)?$",
                                      message = "Valid value must be in the format: <digit>.<digit>.<digit>[-SNAPSHOT]") final String artifactVersionLabel,
                              final Version artifactVersion,
                              final String scmUrl) {
    this.applicationId = applicationId;
    this.applicationName = applicationName;
    this.description = description;
    this.artifactVersionLabel = artifactVersionLabel;
    this.artifactVersion = artifactVersion;
    this.scmUrl = scmUrl;
  }

  public static ApplicationBuildInfo load(String propertiesResourceUrlString, final ClassLoader classLoader) {
    //-- Initialize build-level information for the application. This 'build-info.properties' is generated during
    //-- maven build by the spring-boot-maven-plugin:build-info goal which runs during the 'verify' phase.
    try {
      Map<String, String> applicationInfoPropertyMap = ResourceLoaderUtil.loadPropertiesFromResourceUrlString(
              propertiesResourceUrlString, classLoader);
      String applicationId = applicationInfoPropertyMap.get("applicationId");
      String applicationName = applicationInfoPropertyMap.get("applicationName");
      String description = applicationInfoPropertyMap.get("description");
      String artifactGroupId = applicationInfoPropertyMap.get("artifactGroupId");
      String artifactId = applicationInfoPropertyMap.get("artifactId");
      Version version;
      try {
        Integer majorVersion = Integer.parseInt(applicationInfoPropertyMap.get("majorVersion"));
        Integer minorVersion = Integer.parseInt(applicationInfoPropertyMap.get("minorVersion"));
        Integer incrementalVersion = Integer.parseInt(applicationInfoPropertyMap.get("incrementalVersion"));
        String snapshotQualifier = applicationInfoPropertyMap.get("snapshotQualifier");
        version = new Version(majorVersion, minorVersion, incrementalVersion, snapshotQualifier, artifactGroupId,
                artifactId);
      } catch (RuntimeException re) {
        version = Version.unknownVersion();
      }
      String versionLabel = applicationInfoPropertyMap.get("versionLabel");
      String projectScmUrl = applicationInfoPropertyMap.get("projectScmUrl");
      ApplicationBuildInfo applicationBuildInfo = ApplicationBuildInfo.create(applicationId, applicationName,
              description, versionLabel, version, projectScmUrl);
      return applicationBuildInfo;
    } catch (IOException ioe) {
      LOGGER.warn("Unable to load ApplicationBuildInfo because the file " + propertiesResourceUrlString + " does not exist!)");
      return null;
    }
  }

  public static ApplicationBuildInfo create(final String id, final String name, final String description,
                                            final String artifactVersionLabel, final Version version,
                                            final String scmUrl) {
    return new ApplicationBuildInfo(id, name, description, artifactVersionLabel, version, scmUrl);
  }

  public String getApplicationId() {
    return applicationId;
  }

  public String getApplicationName() {
    return applicationName;
  }

  public String getDescription() {
    return description;
  }

  public String getArtifactVersionLabel() {
    return artifactVersionLabel;
  }

  public Version getArtifactVersion() {
    return artifactVersion;
  }

  public String getScmUrl() {
    return scmUrl;
  }

  @Override
  public String toString() {
    //TODO-Sonar: Should not use JacksonUtil in this class. Forms a cyclical dependency.
    return JacksonUtil.getInstance().serializeObjectToJsonString(this);
  }

}
