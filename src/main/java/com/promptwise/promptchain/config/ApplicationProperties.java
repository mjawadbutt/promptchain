package com.promptwise.promptchain.config;

import com.promptwise.promptchain.PromptChainApplication;
import com.promptwise.promptchain.common.util.ApplicationBuildInfo;
import com.promptwise.promptchain.common.util.ApplicationRuntimeInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.validation.annotation.Validated;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Field names are auto-bound based on matching property names so if field names are changed then property names
 * must be changed accordingly as well. Otherwise, please add @JsonProperty(value = "&lt;property-name>") on each field
 * as needed.
 */
@ConfigurationProperties(prefix = "application")
@Validated
public class ApplicationProperties {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationProperties.class);

  private final String environmentId;
  private final String applicationName;
  private final Integer serverPort;
  private final Path dataDir;
  private final Path tempDir;
  private final String servletContextPath;
  private final Integer sessionTimeoutInSeconds;
  private final DatabaseProperties databaseProperties;
  private final LoggingProperties loggingProperties;
  private final LiquibaseProperties liquibaseProperties;
  private final Integer actuatorPort;
  private final UUID instanceId;
  private final MicroMeterProperties microMeterProperties;

  public ApplicationProperties(
          @Pattern(regexp = "^(winLocal|linuxLocal|prod)$",
                  message = "This is an invalid value! Valid values are winLocal, linuxLocal, OR, prod")
          @NotBlank final String environmentId,
          @NotBlank final String applicationName,
          @NotNull final Integer serverPort,
          @NotNull final Path dataDir,
          final String tempDirPrefix,
          final String servletContextPath,
          @NotNull final Integer sessionTimeoutInSeconds,
          @NotNull final DatabaseProperties databaseProperties,
          @NotNull final LoggingProperties loggingProperties,
          @NotNull final LiquibaseProperties liquibaseProperties,
          @NotNull final Integer actuatorPort,
          @NotNull final MicroMeterProperties microMeterProperties) {
    this.applicationName = applicationName;
    this.environmentId = environmentId;
    this.serverPort = serverPort;
    this.dataDir = dataDir;
    this.servletContextPath = servletContextPath;
    this.sessionTimeoutInSeconds = sessionTimeoutInSeconds;
    this.databaseProperties = databaseProperties;
    this.loggingProperties = loggingProperties;
    this.liquibaseProperties = liquibaseProperties;
    this.actuatorPort = actuatorPort;
    this.instanceId = UUID.randomUUID();
    //-- We are appending the instance ID to address the edge case where more than one LRV-View instance are running
    //-- and the tempDir is a shared storage location which each instance shares. In this case, we each instance
    //-- to have it own tempDir not shared between instances. One reason is that the os-level file-locks acquired (as
    //-- per the imageMagickProperties.maxParallelProcesses property value) should be per LRV-View instance.
    String tempDirPrefixToUse = tempDirPrefix == null ? "tmp" : tempDirPrefix;
    this.tempDir = dataDir.resolve(tempDirPrefixToUse + "-" + instanceId);
    this.microMeterProperties = microMeterProperties;
  }

  @Bean
  public ApplicationBuildInfo applicationBuildInfo() {
    return ApplicationBuildInfo.load(
            "classpath:/com/promptwise/promptchain/project-info.properties", null);
  }

  @Bean
  public ApplicationRuntimeInfo applicationRuntimeInfo(Environment environment) {
    Set<String> activeProfiles = Arrays.stream(environment.getActiveProfiles()).map(String::trim).collect(Collectors.toSet());
    return ApplicationRuntimeInfo.create(activeProfiles);
  }

  public String getEnvironmentId() {
    return environmentId;
  }

  public String getApplicationName() {
    return applicationName;
  }

  public Integer getServerPort() {
    return serverPort;
  }

  public Path getDataDir() {
    return dataDir;
  }

  public UUID getInstanceId() {
    return instanceId;
  }

  public Path getTempDir() {
    return tempDir;
  }

  public String getServletContextPath() {
    return servletContextPath;
  }

  public Integer getSessionTimeoutInSeconds() {
    return sessionTimeoutInSeconds;
  }

  public DatabaseProperties getDatabaseProperties() {
    return databaseProperties;
  }

  public LoggingProperties getLoggingProperties() {
    return loggingProperties;
  }

  public LiquibaseProperties getLiquibaseProperties() {
    return liquibaseProperties;
  }

  public Integer getActuatorPort() {
    return actuatorPort;
  }

  public MicroMeterProperties getMicroMeterProperties() {
    return microMeterProperties;
  }

  @Override
  public String toString() {
    return PromptChainApplication.getJacksonUtil().serializeObjectToJsonString(this);
  }

  public static class DatabaseProperties {
    private final String driverClassName;
    private final String username;
    private final String password;
    private final String url;

    public DatabaseProperties(@NotBlank final String driverClassName, @NotBlank final String username,
                              @NotBlank final String password, @NotBlank final String url) {
      this.driverClassName = driverClassName;
      this.username = username;
      this.password = password;
      this.url = url;
    }

    public String getDriverClassName() {
      return driverClassName;
    }

    public String getUsername() {
      return username;
    }

    public String getPassword() {
      return password;
    }

    public String getUrl() {
      return url;
    }
  }

  public static class LoggingProperties {
    private final String level;
    private final Path rootDir;
    private final String mainLogFileName;
    private final Path mainLogFilePath;
    private final String stacktraceLogFileName;
    private final Path stacktraceLogFilePath;

    public LoggingProperties(@NotNull final String level,
                             @NotNull final Path rootDir,
                             @NotNull final String mainLogFileName,
                             @NotNull final Path mainLogFilePath,
                             @NotNull final String stacktraceLogFileName,
                             @NotNull final Path stacktraceLogFilePath) {
      this.level = level;
      this.rootDir = rootDir;
      this.mainLogFileName = mainLogFileName;
      this.mainLogFilePath = mainLogFilePath;
      this.stacktraceLogFileName = stacktraceLogFileName;
      this.stacktraceLogFilePath = stacktraceLogFilePath;
    }

    public String getLevel() {
      return level;
    }

    public Path getRootDir() {
      return rootDir;
    }

    public String getMainLogFileName() {
      return mainLogFileName;
    }

    public Path getMainLogFilePath() {
      return mainLogFilePath;
    }

    public String getStacktraceLogFileName() {
      return stacktraceLogFileName;
    }

    public Path getStacktraceLogFilePath() {
      return stacktraceLogFilePath;
    }
  }

  public static class LiquibaseProperties {
    private final boolean enabled;

    public LiquibaseProperties(@NotBlank final boolean enabled) {
      this.enabled = enabled;
    }

    public boolean isEnabled() {
      return enabled;
    }
  }

  public static class MicroMeterProperties {
    private final boolean enableJmxMetrics;

    public MicroMeterProperties(boolean enableJmxMetrics) {
      this.enableJmxMetrics = enableJmxMetrics;
    }

    public boolean isEnableJmxMetrics() {
      return enableJmxMetrics;
    }
  }

}
