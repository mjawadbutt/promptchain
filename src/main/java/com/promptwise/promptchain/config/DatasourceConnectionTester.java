package com.promptwise.promptchain.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.sql.Connection;
import java.sql.SQLException;

// Must be a Spring-managed component for @Retryable to work via proxy
@Component
public class DatasourceConnectionTester {

  private static final Logger LOGGER = LoggerFactory.getLogger(DatasourceConnectionTester.class);
  private int attemptCount = 0; // For logging attempts

  @Retryable(
          retryFor = {SQLException.class}, // Retry when SQLException occurs from getConnection()
          maxAttempts = 10,
          backoff = @Backoff(delay = 5000, multiplier = 1.5)
  )
  public HikariDataSource buildAndTestHikariDataSource(@NotNull final String url,
                                                       @NotNull final String username,
                                                       @NotNull final String password,
                                                       @NotNull final String driverClassName,
                                                       @NotNull final Integer hikariConnectionTimeout,
                                                       @NotNull final Integer hikariIdleTimeout,
                                                       @NotNull final Integer hikariMaxLifetime,
                                                       @NotNull final Integer hikariMaximumPoolSize,
                                                       @NotNull final Integer hikariMinimumIdle,
                                                       @NotNull final String hikariPoolName)
          throws SQLException {
    attemptCount++;
    HikariDataSource hikariDataSource = new HikariDataSource();
    hikariDataSource.setJdbcUrl(url);
    hikariDataSource.setUsername(username);
    hikariDataSource.setPassword(password);
    hikariDataSource.setDriverClassName(driverClassName);
    hikariDataSource.setConnectionTimeout(hikariConnectionTimeout);
    hikariDataSource.setIdleTimeout(hikariIdleTimeout);
    hikariDataSource.setMaxLifetime(hikariMaxLifetime);
    hikariDataSource.setMaximumPoolSize(hikariMaximumPoolSize);
    hikariDataSource.setMinimumIdle(hikariMinimumIdle);
    hikariDataSource.setPoolName(hikariPoolName);
    LOGGER.info("Attempt #{} to connect to the datasource: {}", attemptCount, hikariDataSource.getJdbcUrl());
    // Attempt to get a connection to verify the DataSource is working.
    // If this fails, @Retryable will catch the SQLException and retry.
    try (Connection connection = hikariDataSource.getConnection()) {
      LOGGER.info("Successfully connected to the datasource: {} on attempt #{}.", hikariDataSource.getJdbcUrl(),
              attemptCount);
      attemptCount = 0; // Reset for future use if this bean is reused
      return hikariDataSource;
    } catch (SQLException e) {
      LOGGER.warn("Unable to connect on attempt #{}. Retrying... Error: {}", attemptCount, e.getMessage());
      //-- Close the current HikariDataSource to prevent resource leaks and ensure a fresh attempt on retry.
      hikariDataSource.close();
      throw e; // Rethrow to trigger the @Retryable mechanism
    }
  }

  // This method will be called if all @Retryable attempts fail
  @Recover
  public HikariDataSource recover(@NotNull final SQLException e,
                                  @NotNull final String url,
                                  @NotNull final String username) {
    attemptCount = 0; // Reset
    throw new DatasourceConnectionTesterException("Failed to connect to the datasource!", e,
            getAttemptCount(), url, username);
  }

  public int getAttemptCount() {
    return attemptCount;
  }

  public static class DatasourceConnectionTesterException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(DatasourceConnectionTesterException.class);

    private final int failedAttempts;
    private final String url;
    private final String username;

    public static DatasourceConnectionTesterException create(
            @NotNull final String message, @NotNull final Throwable cause,
            @NotNull final int failedAttempts, @NotNull final String url, @NotNull final String username) {
      if (message == null && cause == null) {
        LOGGER.warn("""
                Neither message nor cause have been specified while throwing this! 
                It is recommended to specify at least one of these to make it easier to debug the issue.
                """);
      }
      return new DatasourceConnectionTesterException(message, cause, failedAttempts, url, username);
    }

    private DatasourceConnectionTesterException(@NotNull final String message, @NotNull final Throwable cause,
                                                @NotNull final int failedAttempts, @NotNull final String url,
                                                @NotNull final String username) {
      super(message, cause);
      this.failedAttempts = failedAttempts;
      this.url = url;
      this.username = username;
    }

    public int getFailedAttempts() {
      return failedAttempts;
    }

    public String getUrl() {
      return url;
    }

    public String getUsername() {
      return username;
    }
  }

}