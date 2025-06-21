package com.promptwise.promptchain.common.util;

import com.fasterxml.jackson.core.Version;
import com.promptwise.promptchain.common.util.json.JacksonUtil;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Map;

public class ApplicationBuildInfo {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationBuildInfo.class);

  private final String applicationId;
  private final String applicationName;
  private final String description;
  private final String artifactGroupId;
  private final String artifactId;
  private final String artifactVersionLabel;
  private final Version artifactVersion;
  //    private final String artifactFullVersion;
//    private final Integer artifactMajorVersion;
//    private final Integer artifactMinorVersion;
//    private final Integer artifactIncrementalVersion;
//    private final String artifactSnapshotQualifier;
  private final String scmUrl;

  public ApplicationBuildInfo(@NotNull final String applicationId,
                              @NotNull final String applicationName,
                              @NotNull final String description,
                              @NotNull final String artifactGroupId,
                              @NotNull final String artifactId,
                              @Pattern(regexp = "^[0-9]\\.[0-9]\\.[-0-9](-SNAPSHOT)?$",
                                      message = "Valid value must be in the format: <digit>.<digit>.<digit>[-SNAPSHOT]")
                              @NotNull final String artifactVersionLabel,
                              @NotNull final Version artifactVersion,
                              @NotNull final String scmUrl) {
    Assert.notNull(applicationId, "The 'id' cannot be 'null'!");
    Assert.notNull(applicationName, "The 'name' cannot be 'null'!");
    Assert.notNull(artifactGroupId, "The 'artifactGroupId' cannot be 'null'!");
    Assert.notNull(artifactId, "The 'artifactId' cannot be 'null'!");
    Assert.notNull(artifactVersionLabel, "The 'artifactVersionLabel' cannot be 'null'!");
    Assert.notNull(artifactVersion, "The 'artifactVersion' cannot be 'null'!");
    this.applicationId = applicationId;
    this.applicationName = applicationName;
    this.description = description;
    this.artifactGroupId = artifactGroupId;
    this.artifactId = artifactId;
    this.artifactVersionLabel = artifactVersionLabel;
    this.artifactVersion = artifactVersion;
    this.scmUrl = scmUrl;
  }

  public static ApplicationBuildInfo load(String propertiesResourceUrlString) {
    //-- Initialize build-level information for the application. This 'build-info.properties' is generated during
    //-- maven build by the spring-boot-maven-plugin:build-info goal which runs during the 'verify' phase.
    try {
      Map<String, String> applicationInfoPropertyMap = ResourceLoaderUtil.loadPropertiesFromResourceUrlString(
              propertiesResourceUrlString);
      String applicationId = applicationInfoPropertyMap.get("build.applicationId");
      String applicationName = applicationInfoPropertyMap.get("build.name");
      String description = applicationInfoPropertyMap.get("build.description");
      String artifactGroupId = applicationInfoPropertyMap.get("build.group");
      String artifactId = applicationInfoPropertyMap.get("build.artifact");
      String artifactVersionLabel = applicationInfoPropertyMap.get("build.version");
      int artifactMajorVersion = Integer.parseInt(applicationInfoPropertyMap.get("build.majorVersion"));
      int artifactMinorVersion = Integer.parseInt(applicationInfoPropertyMap.get("build.minorVersion"));
      int artifactIncrementalVersion = Integer.parseInt(applicationInfoPropertyMap.get("build.incrementalVersion"));
      String artifactSnapshotQualifier = applicationInfoPropertyMap.get("build.qualifier");
      String scmUrl = applicationInfoPropertyMap.get("build.scmUrl");
      ApplicationBuildInfo applicationBuildInfo = new ApplicationBuildInfo(applicationId, applicationName, description,
              artifactGroupId, artifactId,
              artifactVersionLabel, new Version(artifactMajorVersion, artifactMinorVersion,
              artifactIncrementalVersion, artifactSnapshotQualifier, artifactGroupId, artifactId), scmUrl);
      return applicationBuildInfo;
    } catch (IOException ioe) {
      LOGGER.warn("Unable to load ApplicationBuildInfo because the file classpath:META-INF/build-info.properties does not exist!)");
      return null;
    }
  }

  public static ApplicationBuildInfo create(@NotNull final String id,
                                            @NotNull final String name,
                                            @NotNull final String description,
                                            @NotNull final String artifactGroupId,
                                            @NotNull final String artifactId,
                                            @NotNull final String artifactVersionLabel,
                                            @NotNull final Integer artifactMajorVersion,
                                            @NotNull final Integer artifactMinorVersion,
                                            @NotNull final Integer artifactIncrementalVersion,
                                            @NotNull final String artifactSnapshotQualifier,
                                            @NotNull final String scmUrl) {
    Version version = new Version(artifactMajorVersion, artifactMinorVersion, artifactIncrementalVersion,
            artifactSnapshotQualifier, artifactGroupId, artifactId);
    return new ApplicationBuildInfo(id, name, description, artifactGroupId, artifactId, artifactVersionLabel,
            version, scmUrl);
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

  public String getArtifactGroupId() {
    return artifactGroupId;
  }

  public String getArtifactId() {
    return artifactId;
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
    return JacksonUtil.getInstance().serializeObjectToJsonString(this);
  }

}
