package com.promptwise.promptchain.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.time.Clock;

@Configuration
public class PromptChainServerConfiguration {

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
    SpringLiquibase liquibase = new SpringLiquibase();
    liquibase.setChangeLog("classpath:liquibase/changelog-master.xml");
    liquibase.setDataSource(dataSource);
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
