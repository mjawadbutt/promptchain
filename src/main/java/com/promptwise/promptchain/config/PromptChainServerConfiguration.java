package com.promptwise.promptchain.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promptwise.promptchain.PromptChainSystemException;
import jakarta.validation.constraints.NotNull;
import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.Clock;

@Configuration
public class PromptChainServerConfiguration {

  private static final Logger LOGGER = LoggerFactory.getLogger(PromptChainServerConfiguration.class);

  private final ApplicationProperties applicationProperties;

  private final ObjectMapper objectMapper;

  public PromptChainServerConfiguration(@NotNull final ApplicationProperties applicationProperties,
                                        @NotNull final ObjectMapper objectMapper) {
    this.applicationProperties = applicationProperties;
    this.objectMapper = objectMapper;
  }

//  @Bean
//  public SpringLiquibase liquibase(DataSource dataSource) {
//    //TODO-Jawad: cleanup liquibase props
//    SpringLiquibase liquibase = new SpringLiquibase();
//
//    // Set the changelog file path
//    liquibase.setChangeLog("classpath:/db/liquibase/db.changelog-master.xml");
//
//    // Set the DataSource for Liquibase to use for schema migrations
//    // This is typically the same DataSource your application uses
//    liquibase.setDataSource(dataSource);
//
//    // You can set other Liquibase properties here if needed, e.g.:
//    liquibase.setLiquibaseSchema("public"); // Or your specific Liquibase schema
//    liquibase.setDefaultSchema("public"); // Or your application's default schema
//
//    return liquibase;
//  }

  @Bean
  public DataSource dataSource(DatasourceConnectionTester connectionTester) { // Inject the helper component
    LOGGER.info("Attempting to test the main datasource with retry logic.");
    ApplicationProperties.DatabaseProperties databaseProperties = getApplicationProperties().getDatabaseProperties();
    try {
      // This call goes through the 'connectionTester' bean's proxy.
      // If connectionTester.buildAndTestHikariDataSource() fails after all retries,
      // its @Recover method (if it exists) will be invoked, or the last exception will be rethrown.
      // In our case, @Recover will throw a RuntimeException if all retries fail.
      return connectionTester.buildAndTestHikariDataSource(databaseProperties.getUrl(),
              databaseProperties.getUsername(), databaseProperties.getPassword(),
              databaseProperties.getDriverClassName(), databaseProperties.getHikariConnectionTimeout(),
              databaseProperties.getHikariIdleTimeout(), databaseProperties.getHikariMaxLifetime(),
              databaseProperties.getHikariMaximumPoolSize(), databaseProperties.getHikariMinimumIdle(),
              databaseProperties.getHikariPoolName());
    } catch (SQLException | RuntimeException e) { // Catch the RuntimeException thrown by @Recover on final failure
      throw PromptChainSystemException.create(e);
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
