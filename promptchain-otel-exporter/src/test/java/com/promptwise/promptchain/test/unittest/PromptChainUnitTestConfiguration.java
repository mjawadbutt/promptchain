package com.promptwise.promptchain.test.unittest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promptwise.promptchain.test.client.PromptChainAdminRestControllerClient;
import com.promptwise.promptchain.test.client.PromptChainRestControllerClient;
import com.promptwise.promptchain.config.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

public class PromptChainUnitTestConfiguration {

  private final String baseUrl;
  private final ApplicationProperties applicationProperties;
  private final ObjectMapper objectMapper;

  @Autowired
  public PromptChainUnitTestConfiguration(
          @Value("${applicationTest.promptChainControllerClient.baseUrl}")
          String baseUrl,
          ApplicationProperties applicationProperties,
          ObjectMapper objectMapper) {
    this.baseUrl = baseUrl;
    this.applicationProperties = applicationProperties;
    this.objectMapper = objectMapper;
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
    //return "http://localhost:" + getApplicationProperties().getServerPort();
    return baseUrl;
  }

  public ApplicationProperties getApplicationProperties() {
    return applicationProperties;
  }

  public ObjectMapper getObjectMapper() {
    return objectMapper;
  }

}
