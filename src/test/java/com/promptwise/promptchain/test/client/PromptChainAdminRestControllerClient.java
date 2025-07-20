package com.promptwise.promptchain.test.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promptwise.promptchain.PromptChainErrorCode;
import com.promptwise.promptchain.common.util.Rfc7807CompliantHttpRequestProcessingErrorResponse;
import com.promptwise.promptchain.controller.PromptChainAdminRestController;
import com.promptwise.promptchain.controller.request.CreateOrUpdateAppUserRequest;
import com.promptwise.promptchain.entity.AppUserEntity;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Set;

public class PromptChainAdminRestControllerClient extends AbstractPromptChainControllerClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(PromptChainAdminRestControllerClient.class);

  public PromptChainAdminRestControllerClient(@NotNull final String baseUrl, @NotNull final ObjectMapper objectMapper) {
    super(baseUrl, objectMapper);
  }

  public AppUserEntity createAppUser(@NotNull final CreateOrUpdateAppUserRequest createOrUpdateAppUserRequest) {
    Assert.notNull(createOrUpdateAppUserRequest, "The parameter 'createOrUpdateAppUserRequest' cannot be 'null'");
    AppUserEntity appUserEntity = invokeRequest(HttpMethod.POST,
            PromptChainAdminRestController.WEB_CONTEXT + "/createAppUser",
            MediaType.APPLICATION_JSON, null, null, MediaType.APPLICATION_JSON,
            (bodySpec) -> {
              return bodySpec
                      .body(createOrUpdateAppUserRequest)
                      .retrieve()
                      .onStatus(httpStatusCode -> httpStatusCode != HttpStatus.OK, (request, response) -> {
                        Rfc7807CompliantHttpRequestProcessingErrorResponse errorResponse = getObjectMapper().readValue(
                                response.getBody(), Rfc7807CompliantHttpRequestProcessingErrorResponse.class);
                        throw new PromptChainErrorResponseException(errorResponse);
                      }).body(AppUserEntity.class);
            });
    return appUserEntity;
  }

  public Set<AppUserEntity> getAllAppUsers() {
    Set<AppUserEntity> appUserEntitySet = invokeRequest(HttpMethod.GET,
            PromptChainAdminRestController.WEB_CONTEXT + "/getAllAppUsers",
            MediaType.APPLICATION_JSON, null, null, MediaType.APPLICATION_JSON,
            (bodySpec) -> {
              return bodySpec
                      .retrieve()
                      .onStatus(httpStatusCode -> httpStatusCode != HttpStatus.OK, (request, response) -> {
                        Rfc7807CompliantHttpRequestProcessingErrorResponse errorResponse = getObjectMapper().readValue(
                                response.getBody(), Rfc7807CompliantHttpRequestProcessingErrorResponse.class);
                        throw new PromptChainErrorResponseException(errorResponse);
                      }).body(new ParameterizedTypeReference<>() {
                      });
            });
    return appUserEntitySet;
  }

  public void deleteAppUser(Long appUserId) {
    Assert.notNull(appUserId, "The parameter 'appUserId' cannot be 'null'!");

    LinkedMultiValueMap<String, String> paramMultiMap = new LinkedMultiValueMap<>();
    paramMultiMap.put("appUserId", Collections.singletonList(String.valueOf(appUserId)));

    invokeRequest(HttpMethod.DELETE, PromptChainAdminRestController.WEB_CONTEXT + "/deleteAppUser",
            MediaType.APPLICATION_JSON, null, paramMultiMap, MediaType.APPLICATION_JSON,
            (bodySpec) -> {
              return bodySpec
                      .retrieve()
                      .onStatus(httpStatusCode -> httpStatusCode != HttpStatus.OK, (request, response) -> {
                        String responseBody = null;
                        try {
                          byte[] bytes = response.getBody().readAllBytes();
                          responseBody = new String(bytes, StandardCharsets.UTF_8);
                          Rfc7807CompliantHttpRequestProcessingErrorResponse errorResponse = getObjectMapper().readValue(
                                  responseBody, Rfc7807CompliantHttpRequestProcessingErrorResponse.class);
                          throw new PromptChainErrorResponseException(errorResponse);
                        } catch (RuntimeException re) {
                          //TODO-Jawad: Fix this later
                          LOGGER.warn("Unable to convert response body: {} to Rfc7807CompliantHttpRequestProcessingErrorResponse",
                                  responseBody);
                          ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(response.getStatusCode(),
                                  responseBody);
                          Rfc7807CompliantHttpRequestProcessingErrorResponse errorResponse =
                                  Rfc7807CompliantHttpRequestProcessingErrorResponse.create(
                                          PromptChainErrorCode.UNKNOWN_ERROR.name(),
                                          problemDetail, null);
                          throw new PromptChainErrorResponseException(errorResponse);
                        }
                      }).body(Void.class);
            });
  }
}
