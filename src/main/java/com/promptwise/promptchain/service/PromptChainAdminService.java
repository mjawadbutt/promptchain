package com.promptwise.promptchain.service;

import com.promptwise.promptchain.common.exception.RequiredResourceNotFoundException;
import com.promptwise.promptchain.common.exception.ResourceAlreadyExistsException;
import com.promptwise.promptchain.entity.AppUserEntity;
import com.promptwise.promptchain.repository.AppUserRepository;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Set;

@Service
@Transactional(readOnly = true)
public class PromptChainAdminService {

  private static final Logger LOGGER = LoggerFactory.getLogger(PromptChainAdminService.class);

  private final AppUserRepository appUserRepository;

  public PromptChainAdminService(@NotNull final AppUserRepository appUserRepository) {
    this.appUserRepository = appUserRepository;
  }

  @Transactional
  public AppUserEntity createAppUser(@NotNull final AppUserEntity appUserEntity)
          throws ResourceAlreadyExistsException {
    Assert.notNull(appUserEntity, "The parameter 'appUserEntity' cannot be 'null'");
    return getAppUserRepository().insertOne(appUserEntity);
  }

  public Set<AppUserEntity> getAllAppUsers() {
    return getAppUserRepository().selectAll();
  }

  @Transactional
  public void updateAppUser(@NotNull AppUserEntity appUserEntity) throws RequiredResourceNotFoundException {
    Assert.notNull(appUserEntity, "The parameter 'appUserEntity' cannot be 'null'");
    getAppUserRepository().updateOne(appUserEntity.getAppUserId(), appUserEntity.getUserName(),
            appUserEntity.getPassword(), appUserEntity.getUserEmail());
  }

  @Transactional
  public boolean deleteAppUser(@NotNull Long appUserId) {
    Assert.notNull(appUserId, "The parameter 'appUserId' cannot be 'null'");
    return getAppUserRepository().deleteOne(appUserId);
  }

  public AppUserRepository getAppUserRepository() {
    return appUserRepository;
  }

}
