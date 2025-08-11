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
  private final CachingProperties cachingProperties;
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
          @NotNull final CachingProperties cachingProperties,
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
    this.cachingProperties = cachingProperties;
    this.liquibaseProperties = liquibaseProperties;
    this.actuatorPort = actuatorPort;
    this.instanceId = UUID.randomUUID();
    //-- We are appending the instance ID to address the edge case where more than one LRV-View instance are running
    //-- and the tempDir is a shared storage location which each instance shares. In this case, we each instance
    //-- to have its own tempDir not shared between instances. One reason is that the os-level file-locks acquired (as
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

  public CachingProperties getCachingProperties() {
    return cachingProperties;
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
    private final Integer hikariConnectionTimeout;
    private final Integer hikariIdleTimeout;
    private final Integer hikariMaxLifetime;
    private final Integer hikariMaximumPoolSize;
    private final Integer hikariMinimumIdle;
    private final String hikariPoolName;

    public DatabaseProperties(@NotBlank final String driverClassName, @NotBlank final String username,
                              @NotBlank final String password, @NotBlank final String url,
                              @NotNull final Integer hikariConnectionTimeout, @NotNull final Integer hikariIdleTimeout,
                              @NotNull final Integer hikariMaxLifetime, @NotNull final Integer hikariMaximumPoolSize,
                              @NotNull final Integer hikariMinimumIdle, @NotNull final String hikariPoolName) {
      this.driverClassName = driverClassName;
      this.username = username;
      this.password = password;
      this.url = url;
      this.hikariConnectionTimeout = hikariConnectionTimeout;
      this.hikariIdleTimeout = hikariIdleTimeout;
      this.hikariMaxLifetime = hikariMaxLifetime;
      this.hikariMaximumPoolSize = hikariMaximumPoolSize;
      this.hikariMinimumIdle = hikariMinimumIdle;
      this.hikariPoolName = hikariPoolName;
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

    public Integer getHikariConnectionTimeout() {
      return hikariConnectionTimeout;
    }

    public Integer getHikariIdleTimeout() {
      return hikariIdleTimeout;
    }

    public Integer getHikariMaxLifetime() {
      return hikariMaxLifetime;
    }

    public Integer getHikariMaximumPoolSize() {
      return hikariMaximumPoolSize;
    }

    public Integer getHikariMinimumIdle() {
      return hikariMinimumIdle;
    }

    public String getHikariPoolName() {
      return hikariPoolName;
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

  public static class CachingProperties {
    private final Boolean redisEnabled;
    private final String redisHost;
    private final Integer redisPort;
    private final String redisPassword;
    private final Integer redisConnectionPoolSize;
    private final Integer redisConnectionMinimumIdleSize;
    private final Integer redisTimeout;

    public CachingProperties(@NotNull final Boolean redisEnabled, @NotNull final String redisHost,
                             @NotNull final Integer redisPort, final String redisPassword,
                             @NotNull final Integer redisConnectionPoolSize,
                             @NotNull final Integer redisConnectionMinimumIdleSize,
                             @NotNull final Integer redisTimeout) {
      this.redisEnabled = redisEnabled;
      this.redisHost = redisHost;
      this.redisPort = redisPort;
      this.redisPassword = redisPassword;
      this.redisConnectionPoolSize = redisConnectionPoolSize;
      this.redisConnectionMinimumIdleSize = redisConnectionMinimumIdleSize;
      this.redisTimeout = redisTimeout;
    }

    public Boolean getRedisEnabled() {
      return redisEnabled;
    }

    public String getRedisHost() {
      return redisHost;
    }

    public Integer getRedisPort() {
      return redisPort;
    }

    public String getRedisPassword() {
      return redisPassword;
    }

    public Integer getRedisConnectionPoolSize() {
      return redisConnectionPoolSize;
    }

    public Integer getRedisConnectionMinimumIdleSize() {
      return redisConnectionMinimumIdleSize;
    }

    public Integer getRedisTimeout() {
      return redisTimeout;
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
