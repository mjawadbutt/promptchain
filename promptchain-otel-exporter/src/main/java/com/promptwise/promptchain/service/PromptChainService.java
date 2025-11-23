package com.promptwise.promptchain.service;

import com.promptwise.promptchain.entity.AppUserEntity;
import com.promptwise.promptchain.repository.AppUserRepository;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PromptChainService {

  private static final Logger LOGGER = LoggerFactory.getLogger(PromptChainService.class);

  private final AppUserRepository appUserRepository;

  public PromptChainService(@NotNull final AppUserRepository appUserRepository) {
    this.appUserRepository = appUserRepository;
  }

  public AppUserEntity getAppUser(@NotNull final Long appUserId) {
    return getAppUserRepository().selectOne(appUserId).orElse(null);
  }

  public AppUserRepository getAppUserRepository() {
    return appUserRepository;
  }

}
