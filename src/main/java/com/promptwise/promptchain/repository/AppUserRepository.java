package com.promptwise.promptchain.repository;

import com.promptwise.promptchain.common.exception.DatabaseAccessException;
import com.promptwise.promptchain.common.exception.RequiredResourceNotFoundException;
import com.promptwise.promptchain.common.exception.ResourceAlreadyExistsException;
import com.promptwise.promptchain.entity.AppUserEntity;
import com.promptwise.promptchain.generated.jooq.tables.records.AppUserRecord;
import jakarta.validation.constraints.NotNull;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.promptwise.promptchain.generated.jooq.tables.AppUser.*;

@Repository
@Transactional(readOnly = true)
public class AppUserRepository {

  private static final Logger LOGGER = LoggerFactory.getLogger(AppUserRepository.class);

  private final DSLContext dslContext;

  @Autowired
  public AppUserRepository(@NotNull final DSLContext dslContext) {
    this.dslContext = dslContext;
  }

  @Transactional
  public AppUserEntity insertOne(@NotNull final AppUserEntity appUserEntity) throws ResourceAlreadyExistsException {
    Assert.notNull(appUserEntity, "The parameter 'appUserEntity' cannot be 'null'");
    //-- Assert whether the AppUserEntity already exists so that we can handle it as a user-level (i.e. checked)
    //-- exception. Otherwise, if we skip this step, then it will fail as a database-level foreign-key violation
    //-- (i.e. runtime) exception during 'insert'.
    Optional<AppUserEntity> optionalAppUserEntity = selectOneByUserEmail(appUserEntity.getUserEmail());
    if (optionalAppUserEntity.isPresent()) {
      throw ResourceAlreadyExistsException.create("APP_USER", "USER_EMAIL", appUserEntity.getUserEmail());
    } else {
      try {
        // Use the jOOQ generated table object APP_USER
        AppUserRecord r = getDslContext().insertInto(APP_USER)
                .set(APP_USER.USER_NAME, appUserEntity.getUserName())
                .set(APP_USER.PASSWORD, appUserEntity.getPassword())
                .set(APP_USER.USER_EMAIL, appUserEntity.getUserEmail())
                .returning(APP_USER.APP_USER_ID) // Ask DB to return the generated ID
                .fetchSingle();
        LOGGER.debug("Inserting {}", appUserEntity);
        // Fetch the complete entity with generated ID and database-computed timestamps
        AppUserEntity inserted = selectOneRequired(r.getAppUserId());
        LOGGER.debug("Inserted {}", inserted);
        return inserted;
      } catch (RequiredResourceNotFoundException | RuntimeException e) {
        // Wrap specific exceptions or general runtime exceptions
        throw DatabaseAccessException.create(e);
      }
    }
  }

  public Set<AppUserEntity> selectAll() {
    try {
      Set<AppUserEntity> entitySet = getDslContext().select()
              .from(APP_USER)
              .fetch()
              .into(AppUserEntity.class)
              .parallelStream()
              .collect(Collectors.toSet());
      return entitySet;
    } catch (RuntimeException re) {
      throw DatabaseAccessException.create(re);
    }
  }

  public @NotNull AppUserEntity selectOneRequired(@NotNull final Long appUserId)
          throws RequiredResourceNotFoundException {
    Optional<AppUserEntity> optionalAppUserEntity = selectOne(appUserId);
    return optionalAppUserEntity.orElseThrow(
            () -> RequiredResourceNotFoundException.create("APP_USER", "APP_USER_ID",
                    String.valueOf(appUserId)));
  }

  public Optional<AppUserEntity> selectOne(@NotNull final Long appUserId) {
    Assert.notNull(appUserId, "The parameter 'appUserId' cannot be 'null'");
    try {
      Optional<AppUserEntity> optionalAppUserEntity = getDslContext().select()
              .from(APP_USER)
              .where(APP_USER.APP_USER_ID.eq(appUserId))
              .fetchOptionalInto(AppUserEntity.class);
      return optionalAppUserEntity;
    } catch (RuntimeException re) {
      throw DatabaseAccessException.create(re);
    }
  }

  public Optional<AppUserEntity> selectOneByUserEmail(@NotNull final String userEmail) {
    Assert.notNull(userEmail, "The parameter 'userEmail' cannot be 'null'");
    try {
      Optional<AppUserEntity> optionalAppUserEntity = getDslContext().select()
              .from(APP_USER)
              .where(APP_USER.USER_EMAIL.eq(userEmail))
              .fetchOptionalInto(AppUserEntity.class);
      return optionalAppUserEntity;
    } catch (RuntimeException re) {
      throw DatabaseAccessException.create(re);
    }
  }

  @Transactional
  public boolean updateOne(@NotNull final Long appUserId, final String userName, final String password,
                           final String userEmail)
          throws RequiredResourceNotFoundException {
    Assert.notNull(appUserId, "The parameter 'appUserId' cannot be 'null'");

    Optional<AppUserEntity> optionalAppUserEntity = selectOneByUserEmail(userEmail);
    if (optionalAppUserEntity.isPresent()) {
      throw RequiredResourceNotFoundException.create("APP_USER", "APP_USER_ID", appUserId.toString());
    } else {

      try {
        Map<TableField<AppUserRecord, ?>, Object> fieldValuesMap = new HashMap<>();
        if (userName != null) {
          fieldValuesMap.put(APP_USER.USER_NAME, userName);
        }
        if (password != null) {
          fieldValuesMap.put(APP_USER.PASSWORD, password);
        }
        if (userEmail != null) {
          fieldValuesMap.put(APP_USER.USER_EMAIL, userEmail);
        }
        int numberOfEntitiesUpdated = getDslContext().update(APP_USER)
                .set(fieldValuesMap)
                .where(APP_USER.APP_USER_ID.eq(appUserId))
                .execute();
        return RepositoryUtils.validateSingleUpdate(numberOfEntitiesUpdated);
      } catch (RuntimeException re) {
        throw DatabaseAccessException.create(re);
      }
    }
  }

  @Transactional
  public boolean deleteOne(@NotNull final Long appUserId) {
    Assert.notNull(appUserId, "The parameter 'appUserId' cannot be 'null'");
    try {
      int numberOfEntitiesDeleted = getDslContext().deleteFrom(APP_USER)
              .where(APP_USER.APP_USER_ID.eq(appUserId)).execute();
      return RepositoryUtils.validateSingleDelete(numberOfEntitiesDeleted);
    } catch (RuntimeException re) {
      throw DatabaseAccessException.create(re);
    }
  }

  public DSLContext getDslContext() {
    return dslContext;
  }

}
