package com.promptwise.promptchain.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
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
    liquibase.setChangeLog("classpath:liquibase/changelog-master.xml");

    // Set the DataSource for Liquibase to use for schema migrations
    // This is typically the same DataSource your application uses
    liquibase.setDataSource(dataSource);

    // You can set other Liquibase properties here if needed, e.g.:
    liquibase.setShouldRun(true); // Default is true, but good for clarity
    liquibase.setLiquibaseSchema("public"); // Or your specific Liquibase schema
    liquibase.setDefaultSchema("public"); // Or your application's default schema

    return liquibase;
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
