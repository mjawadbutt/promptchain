package com.promptwise.promptchain.test.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promptwise.promptchain.common.util.Rfc7807CompliantHttpRequestProcessingErrorResponse;
import com.promptwise.promptchain.controller.PromptChainRestController;
import com.promptwise.promptchain.entity.AppUserEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Collections;

public class PromptChainRestControllerClient extends AbstractPromptChainControllerClient {

  public PromptChainRestControllerClient(@NotNull final String baseUrl, @NotNull final ObjectMapper objectMapper) {
    super(baseUrl, objectMapper);
  }

  public AppUserEntity getAppUser(long appUserId) {
    Assert.notNull(appUserId, "The parameter 'appUserId' cannot be 'null'!");

    LinkedMultiValueMap<String, String> paramMultiMap = new LinkedMultiValueMap<>();
    paramMultiMap.put("appUserId", Collections.singletonList(String.valueOf(appUserId)));
    AppUserEntity appUserEntity = invokeRequest(HttpMethod.GET,
            PromptChainRestController.WEB_CONTEXT + "/getAppUser", MediaType.APPLICATION_JSON,
            null, paramMultiMap, MediaType.APPLICATION_JSON,
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
    return appUserEntity;
  }

  public String ping() {
    String pingResponse = invokeRequest(HttpMethod.GET,
            PromptChainRestController.WEB_CONTEXT + "/ping", MediaType.APPLICATION_JSON,
            null, null, MediaType.APPLICATION_JSON,
            (bodySpec) -> {
              return bodySpec
                      .retrieve()
                      .onStatus(httpStatusCode -> httpStatusCode != HttpStatus.OK, (request, response) -> {
                        Rfc7807CompliantHttpRequestProcessingErrorResponse errorResponse = getObjectMapper().readValue(
                                response.getBody(), Rfc7807CompliantHttpRequestProcessingErrorResponse.class);
                        throw new PromptChainErrorResponseException(errorResponse);
                      }).body(String.class);
            });
    return pingResponse;
  }

}
