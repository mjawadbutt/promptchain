package com.promptchain.test.integrationtest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promptchain.test.client.PromptChainAdminRestControllerClient;
import com.promptchain.test.client.PromptChainRestControllerClient;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

@PromptChainIntegrationTestClass
class PromptChainIntegrationTests {

  private static final Logger LOGGER = LoggerFactory.getLogger(PromptChainIntegrationTests.class);

  private final PromptChainRestControllerClient promptChainRestControllerClient;
  private final PromptChainAdminRestControllerClient promptChainAdminRestControllerClient;
  private final ObjectMapper objectMapper;
  private final TestRestTemplate restTemplate;

  @Autowired
  public PromptChainIntegrationTests(@NotNull final PromptChainRestControllerClient promptChainRestControllerClient,
                                     @NotNull final PromptChainAdminRestControllerClient promptChainAdminRestControllerClient,
                                     @NotNull final ObjectMapper objectMapper,
                                     @NotNull final TestRestTemplate restTemplate) {
    this.promptChainRestControllerClient = promptChainRestControllerClient;
    this.promptChainAdminRestControllerClient = promptChainAdminRestControllerClient;
    this.objectMapper = objectMapper;
    this.restTemplate = restTemplate;
  }

  @BeforeAll
  void beforeRunningAnyTestCase() {
  }

  @Test
  @DisplayName("PromptChainRestControllerClient.getClient: Retrieves the basic details of the client having the given client-id.")
  @Tags({@Tag("Group:IntegrationTests")})
  void testGetClient() {
    int clientId = 1;
    ClientEntity actualResult = getPromptChainRestControllerClient().getClient(clientId);
    System.out.println();
  }

  @AfterAll
  public void afterRunningAllTestCases() {
//    LOGGER.debug("Shutting down the application server.");
//    stopHsqlServer();
//    LOGGER.debug("Successfully shut down the application server.");
  }

  public PromptChainRestControllerClient getPromptChainRestControllerClient() {
    return promptChainRestControllerClient;
  }

  public PromptChainAdminRestControllerClient getPromptChainAdminRestControllerClient() {
    return promptChainAdminRestControllerClient;
  }

  public ObjectMapper getObjectMapper() {
    return objectMapper;
  }

  public TestRestTemplate getRestTemplate() {
    return restTemplate;
  }

}
