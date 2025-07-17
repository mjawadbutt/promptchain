package com.promptwise.promptchain.test.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promptwise.promptchain.common.util.Rfc7807CompliantHttpRequestProcessingErrorResponse;
import com.promptwise.promptchain.controller.PromptChainAdminRestController;
import com.promptwise.promptchain.controller.request.CreateOrUpdateAppUserRequest;
import com.promptwise.promptchain.entity.AppUserEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;

import java.util.Set;

import static com.promptwise.promptchain.PromptChainApplication.*;

public class PromptChainAdminRestControllerClient extends AbstractIPromptChainControllerClient {

  public PromptChainAdminRestControllerClient(@NotNull final String baseUrl, @NotNull final ObjectMapper objectMapper) {
    super(baseUrl, objectMapper);
  }

  public AppUserEntity createAppUser(@NotNull final CreateOrUpdateAppUserRequest createOrUpdateAppUserRequest) {
    Assert.notNull(createOrUpdateAppUserRequest, "The parameter 'createOrUpdateAppUserRequest' cannot be 'null'");
    AppUserEntity appUserEntity = invokeRequest(HttpMethod.POST,
            PromptChainAdminRestController.WEB_CONTEXT + "/createClient", MediaType.APPLICATION_JSON,
            null, null, MediaType.APPLICATION_JSON,
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
            WEB_CONTEXT + "/getAllAppUsers", MediaType.APPLICATION_JSON, null,
            null, MediaType.APPLICATION_JSON,
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

}
