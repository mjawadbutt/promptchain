package com.promptwise.promptchain.test.client;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;

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
            PromptChainAdminRestController.URI + "/createAppUser",
            MediaType.APPLICATION_JSON, null, null, MediaType.APPLICATION_JSON,
            (bodySpec) -> {
              return bodySpec
                      .body(createOrUpdateAppUserRequest)
                      .retrieve()
                      .onStatus(httpStatusCode -> httpStatusCode != HttpStatus.OK,
                              this::handleErrorResponse).body(AppUserEntity.class);
            });
    return appUserEntity;
  }

  public Set<AppUserEntity> getAllAppUsers() {
    Set<AppUserEntity> appUserEntitySet = invokeRequest(HttpMethod.GET,
            PromptChainAdminRestController.URI + "/getAllAppUsers",
            MediaType.APPLICATION_JSON, null, null, MediaType.APPLICATION_JSON,
            (bodySpec) -> {
              return bodySpec
                      .retrieve()
                      .onStatus(httpStatusCode -> httpStatusCode != HttpStatus.OK,
                              this::handleErrorResponse).body(new ParameterizedTypeReference<>() {
                      });
            });
    return appUserEntitySet;
  }

  public void deleteAppUser(Long appUserId) {
    Assert.notNull(appUserId, "The parameter 'appUserId' cannot be 'null'!");

    LinkedMultiValueMap<String, String> paramMultiMap = new LinkedMultiValueMap<>();
    paramMultiMap.put("appUserId", Collections.singletonList(String.valueOf(appUserId)));

    invokeRequest(HttpMethod.DELETE, PromptChainAdminRestController.URI + "/deleteAppUser",
            MediaType.APPLICATION_JSON, null, paramMultiMap, MediaType.APPLICATION_JSON,
            (bodySpec) -> {
              return bodySpec
                      .retrieve()
                      .onStatus(httpStatusCode -> httpStatusCode != HttpStatus.OK,
                              this::handleErrorResponse).body(Void.class);
            });
  }
}
