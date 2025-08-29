package com.promptwise.promptchain.controller;

import com.promptwise.promptchain.PromptChainApplication;
import com.promptwise.promptchain.entity.AppUserEntity;
import com.promptwise.promptchain.service.PromptChainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PromptChainRestController.URI)
public class PromptChainRestController {

  public static final String URI = PromptChainApplication.URI__API;
  private static final Logger LOGGER = LoggerFactory.getLogger(PromptChainRestController.class);

  private final PromptChainService promptChainService;

  public PromptChainRestController(final PromptChainService promptChainService) {
    this.promptChainService = promptChainService;
  }

  @GetMapping(path = "/getAppUser", produces = MediaType.APPLICATION_JSON_VALUE)
  public AppUserEntity getAppUser(@RequestParam final Long appUserId) {
    AppUserEntity appUserEntity = getPromptChainService().getAppUser(appUserId);
    return appUserEntity;
  }

  @GetMapping(path = "/ping", produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> ping() {
    return ResponseEntity.ok("pong");
  }

  public PromptChainService getPromptChainService() {
    return promptChainService;
  }

}
