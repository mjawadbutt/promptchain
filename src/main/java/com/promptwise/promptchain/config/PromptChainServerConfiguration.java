package com.promptwise.promptchain.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.validation.constraints.NotNull;
import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Clock;

@Configuration
public class PromptChainServerConfiguration {

  private static final Logger LOGGER = LoggerFactory.getLogger(PromptChainServerConfiguration.class);

  private final ApplicationProperties applicationProperties;

  private final ObjectMapper objectMapper;

  @Autowired
  public PromptChainServerConfiguration(@NotNull final ApplicationProperties applicationProperties,
                                        @NotNull final ObjectMapper objectMapper) {
    this.applicationProperties = applicationProperties;
    this.objectMapper = objectMapper;
  }

  @Bean
  public SpringLiquibase liquibase(DataSource dataSource) {
    LOGGER.info(toString());

    SpringLiquibase liquibase = new SpringLiquibase();

    // Set the changelog file path
    liquibase.setChangeLog("classpath:/db/liquibase/db.changelog-master.xml");

    // Set the DataSource for Liquibase to use for schema migrations
    // This is typically the same DataSource your application uses
    liquibase.setDataSource(dataSource);

    // You can set other Liquibase properties here if needed, e.g.:
    liquibase.setShouldRun(true); // Default is true, but good for clarity
    liquibase.setLiquibaseSchema("public"); // Or your specific Liquibase schema
    liquibase.setDefaultSchema("public"); // Or your application's default schema

    return liquibase;
  }

  @Retryable(
          retryFor = {SQLException.class}, // Retry when SQLException occurs
          maxAttempts = 10, // Maximum number of retry attempts
          backoff = @Backoff(delay = 5000, multiplier = 1.5) // Initial delay 5s, then 7.5s, 11.25s, etc.
  )
  @Bean
  public DataSource dataSource() {
    try {
      // Call the @Retryable method. Spring's AOP proxy will handle retries.
      // If the retries are exhausted, createAndTestDataSource() will re-throw SQLException.
      return createAndTestDataSource();
    } catch (SQLException e) {
      // This catch block handles the SQLException that is re-thrown by createAndTestDataSource()
      // *after* all retries have been exhausted by the @Retryable proxy.
      // It wraps it in a RuntimeException, which is acceptable for a @Bean method.
      LOGGER.error("FATAL: Database connection could not be established after all retries. Application will not start. Error: " + e.getMessage());
      throw new RuntimeException("Failed to initialize DataSource after retries", e);
    }
  }

  public DataSource createAndTestDataSource() throws SQLException {
    LOGGER.info("Attempting to create and test DataSource connection...");
    HikariDataSource hikariDataSource = new HikariDataSource();
    hikariDataSource.setJdbcUrl(getApplicationProperties().getDatabaseProperties().getUrl());
    hikariDataSource.setUsername(getApplicationProperties().getDatabaseProperties().getUsername());
    hikariDataSource.setPassword(getApplicationProperties().getDatabaseProperties().getPassword());
    hikariDataSource.setDriverClassName(getApplicationProperties().getDatabaseProperties().getDriverClassName());
    hikariDataSource.setConnectionTimeout(30000);
    hikariDataSource.setIdleTimeout(30000);
    hikariDataSource.setMaxLifetime(2000000);
    hikariDataSource.setMaximumPoolSize(20);
    hikariDataSource.setMinimumIdle(5);
    hikariDataSource.setPoolName("SpringBootJPAHikariCP");

    // Attempt to get a connection to verify the DataSource is working.
    // If this fails, @Retryable will catch the SQLException and retry.
    try (Connection connection = hikariDataSource.getConnection()) {
      System.out.println("Successfully obtained initial connection to the database. DataSource ready.");
      return hikariDataSource;
    } catch (SQLException e) {
      System.err.println("Failed to obtain initial database connection. Retrying... Error: " + e.getMessage());
      // Important: Close the current HikariDataSource instance to prevent resource leaks
      // before the retry mechanism creates a new attempt.
      hikariDataSource.close();
      throw e; // Re-throw the exception to trigger the @Retryable mechanism
    }
  }

  @Bean
  public Clock clock() {
    return Clock.systemDefaultZone();
  }

  public ApplicationProperties getApplicationProperties() {
    return applicationProperties;
  }

  public ObjectMapper getObjectMapper() {
    return objectMapper;
  }

}
