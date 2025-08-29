package com.promptwise.promptchain.controller;

import com.promptwise.promptchain.PromptChainApplication;
import com.promptwise.promptchain.common.exception.RequiredResourceNotFoundException;
import com.promptwise.promptchain.common.exception.ResourceAlreadyExistsException;
import com.promptwise.promptchain.controller.request.CreateOrUpdateAppUserRequest;
import com.promptwise.promptchain.entity.AppUserEntity;
import com.promptwise.promptchain.service.PromptChainAdminService;
import com.promptwise.promptchain.service.PromptChainService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Hidden
@RestController
@RequestMapping(value = PromptChainAdminRestController.URI)
public class PromptChainAdminRestController {

  public static final String URI = PromptChainApplication.URI__API + "/admin";
  private static final Logger LOGGER = LoggerFactory.getLogger(PromptChainAdminRestController.class);

  private final PromptChainAdminService promptChainAdminService;
  private final PromptChainService promptChainService;

  public PromptChainAdminRestController(@NotNull final PromptChainAdminService promptChainAdminService,
                                        @NotNull final PromptChainService promptChainService) {
    this.promptChainAdminService = promptChainAdminService;
    this.promptChainService = promptChainService;
  }

  @PostMapping(path = "/createAppUser",
          consumes = {MediaType.APPLICATION_JSON_VALUE},
          produces = MediaType.APPLICATION_JSON_VALUE)
  public AppUserEntity createAppUser(@Valid @RequestBody final CreateOrUpdateAppUserRequest request)
          throws ResourceAlreadyExistsException {
    AppUserEntity saved = getPromptChainAdminService().createAppUser(request.appUserEntity());
    return saved;
  }

  @GetMapping(path = "/getAllAppUsers", produces = MediaType.APPLICATION_JSON_VALUE)
  public Set<AppUserEntity> getAllAppUsers() {
    Set<AppUserEntity> appUserEntitySet = getPromptChainAdminService().getAllAppUsers();
    return appUserEntitySet;
  }

  @PutMapping(path = "/updateAppUser",
          consumes = {MediaType.APPLICATION_JSON_VALUE},
          produces = MediaType.APPLICATION_JSON_VALUE)
  public void updateAppUser(@Valid @RequestBody final CreateOrUpdateAppUserRequest request)
          throws RequiredResourceNotFoundException {
    getPromptChainAdminService().updateAppUser(request.appUserEntity());
  }

  @DeleteMapping(path = "/deleteAppUser")
  public void deleteApplication(@RequestParam @NotNull final Long appUserId) {
    getPromptChainAdminService().deleteAppUser(appUserId);
  }

  public PromptChainAdminService getPromptChainAdminService() {
    return promptChainAdminService;
  }

  public PromptChainService getPromptChainService() {
    return promptChainService;
  }

}
