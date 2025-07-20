package com.promptwise.promptchain.test.integrationtest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promptwise.promptchain.config.ApplicationProperties;
import com.promptwise.promptchain.controller.request.CreateOrUpdateAppUserRequest;
import com.promptwise.promptchain.entity.AppUserEntity;
import com.promptwise.promptchain.test.client.PromptChainAdminRestControllerClient;
import com.promptwise.promptchain.test.client.PromptChainRestControllerClient;
import jakarta.validation.constraints.NotNull;
import org.assertj.core.api.Assertions;
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
  private final ApplicationProperties applicationProperties;
  private final ObjectMapper objectMapper;
  private final TestRestTemplate restTemplate;

  @Autowired
  public PromptChainIntegrationTests(@NotNull final PromptChainRestControllerClient promptChainRestControllerClient,
                                     @NotNull final PromptChainAdminRestControllerClient promptChainAdminRestControllerClient,
                                     @NotNull final ApplicationProperties applicationProperties,
                                     @NotNull final ObjectMapper objectMapper,
                                     @NotNull final TestRestTemplate restTemplate) {
    this.promptChainRestControllerClient = promptChainRestControllerClient;
    this.promptChainAdminRestControllerClient = promptChainAdminRestControllerClient;
    this.applicationProperties = applicationProperties;
    this.objectMapper = objectMapper;
    this.restTemplate = restTemplate;
  }

  @BeforeAll
  void beforeRunningAnyTestCase() {
  }

  @Test
  @DisplayName("PromptChainRestControllerClient.getClient: Retrieves the basic details of the client having the given client-id.")
  @Tags({@Tag("Group:IntegrationTests")})
  void testGetAppUser() {
    CreateOrUpdateAppUserRequest createOrUpdateAppUserRequest = new CreateOrUpdateAppUserRequest(
            AppUserEntity.createForInsertOrUpdate("jawad", "abcd", "jawad@promptchain.com"));
    AppUserEntity actualResult = getPromptChainAdminRestControllerClient().createAppUser(createOrUpdateAppUserRequest);

    AppUserEntity expectedResult = AppUserEntity.createForSelect(actualResult.getUserId(),
            createOrUpdateAppUserRequest.appUserEntity().getUserName(),
            createOrUpdateAppUserRequest.appUserEntity().getPassword(),
            createOrUpdateAppUserRequest.appUserEntity().getUserEmail(),
            actualResult.getCreatedAt(), actualResult.getLastUpdatedAt());

    getPromptChainAdminRestControllerClient().deleteAppUser(actualResult.getUserId());
    Assertions.assertThat(actualResult).usingRecursiveComparison().isEqualTo(expectedResult);
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

  public ApplicationProperties getApplicationProperties() {
    return applicationProperties;
  }

  public ObjectMapper getObjectMapper() {
    return objectMapper;
  }

  public TestRestTemplate getRestTemplate() {
    return restTemplate;
  }

}
