package com.promptchain.test.integrationtest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promptchain.test.client.PromptChainAdminRestControllerClient;
import com.promptchain.test.client.PromptChainRestControllerClient;
import com.promptwise.promptchain.config.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

public class PromptChainIntegrationTestConfiguration {

  private final ApplicationProperties applicationProperties;
  private final ObjectMapper objectMapper;
  private final String baseUrl;

  @Autowired
  public PromptChainIntegrationTestConfiguration(
          ApplicationProperties applicationProperties,
          ObjectMapper objectMapper,
          @Value("${applicationTestIntegration.iflowControllerClient.baseUrl}")
          String baseUrl) {
    this.applicationProperties = applicationProperties;
    this.objectMapper = objectMapper;
    this.baseUrl = baseUrl;
  }

  @Bean
  public PromptChainRestControllerClient promptChainRestControllerClient() {
    return new PromptChainRestControllerClient(getBaseUrl(), getObjectMapper());
  }

  @Bean
  public PromptChainAdminRestControllerClient promptChainAdminRestControllerClient() {
    return new PromptChainAdminRestControllerClient(getBaseUrl(), getObjectMapper());
  }

  @Bean
  public RestClient createRestClient() {
    return RestClient.create(getBaseUrl());
  }

  public String getBaseUrl() {
    return baseUrl;
  }

  public ApplicationProperties getApplicationProperties() {
    return applicationProperties;
  }

  public ObjectMapper getObjectMapper() {
    return objectMapper;
  }

}
