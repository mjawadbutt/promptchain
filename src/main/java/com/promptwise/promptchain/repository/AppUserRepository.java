package com.promptwise.promptchain.repository;

import com.promptwise.promptchain.common.exception.DatabaseAccessException;
import com.promptwise.promptchain.common.exception.RequiredResourceNotFoundException;
import com.promptwise.promptchain.entity.AppUserEntity;
import jakarta.validation.constraints.NotNull;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class AppUserRepository {

  private final DSLContext dslContext;

  @Autowired
  public AppUserRepository(@NotNull final DSLContext dslContext) {
    this.dslContext = dslContext;
  }

  /**
   * Inserts a new AppUserEntity into the database.
   *
   * @param appUserEntity The AppUserEntity to insert. Must not be null.
   * @return The inserted AppUserEntity with its generated ID and default values.
   * @throws DatabaseAccessException if a database access error occurs.
   */
  @Transactional
  public AppUserEntity insertOne(@NotNull final AppUserEntity appUserEntity) {
    Assert.notNull(appUserEntity, "The parameter 'appUserEntity' cannot be 'null'");
    try {
      // Use the jOOQ generated table object APP_USER
      AppUserRecord r = getDslContext().insertInto(APP_USER)
              .set(APP_USER.USER_EMAIL, appUserEntity.getUserEmail())
              .set(APP_USER.PASSWORD, appUserEntity.getPassword())
              .set(APP_USER.USER_NAME, appUserEntity.getUserName())
              // created_at and last_updated_at have DEFAULT NOW() in DB, so no need to set them here
              // unless you want to override the default.
              // If you want to ensure they are set to current Instant from app side:
              // .set(APP_USER.CREATED_AT, appUserEntity.getCreatedAt() != null ? appUserEntity.getCreatedAt() : Instant.now())
              // .set(APP_USER.LAST_UPDATED_AT, appUserEntity.getLastUpdatedAt() != null ? appUserEntity.getLastUpdatedAt() : Instant.now())
              .returning(APP_USER.USER_ID) // Ask DB to return the generated ID
              .fetchSingle();

      // Fetch the complete entity with generated ID and database-computed timestamps
      return selectOneRequired(r.getUserId());
    } catch (RequiredResourceNotFoundException | RuntimeException e) {
      // Wrap specific exceptions or general runtime exceptions
      throw DatabaseAccessException.create(e);
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

  public @NotNull AppUserEntity selectOneRequired(@NotNull final Long userId)
          throws RequiredResourceNotFoundException {
    Optional<AppUserEntity> optionalAppUserEntity = selectOne(userId);
    return optionalAppUserEntity.orElseThrow(
            () -> RequiredResourceNotFoundException.createForUserId(userId));
  }

  /**
   * Retrieves an optional AppUserEntity by its ID.
   *
   * @param userId The ID of the user to retrieve. Must not be null.
   * @return An Optional containing the AppUserEntity if found, or empty otherwise.
   * @throws DatabaseAccessException if a database access error occurs.
   */
  public Optional<AppUserEntity> selectOne(@NotNull final Long userId) {
    Assert.notNull(userId, "The parameter 'userId' cannot be 'null'");
    try {
      Optional<AppUserEntity> optionalAppUserEntity = getDslContext().select()
              .from(APP_USER)
              .where(APP_USER.USER_ID.eq(userId))
              .fetchOptionalInto(AppUserEntity.class);
      return optionalAppUserEntity;
    } catch (RuntimeException re) {
      throw DatabaseAccessException.create(re);
    }
  }

  /**
   * Retrieves an optional AppUserEntity by its email.
   *
   * @param userEmail The email of the user to retrieve. Must not be null.
   * @return An Optional containing the AppUserEntity if found, or empty otherwise.
   * @throws DatabaseAccessException if a database access error occurs.
   */
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

  /**
   * Updates an existing AppUserEntity.
   *
   * @param userId        The ID of the user to update. Must not be null.
   * @param userEmail     The new email (optional).
   * @param password      The new password (optional).
   * @param userName      The new user name (optional).
   * @param lastUpdatedAt The new last updated timestamp (optional, will default to NOW() if null).
   * @return true if exactly one entity was updated, false otherwise.
   * @throws DatabaseAccessException if a database access error occurs.
   */
  @Transactional
  public boolean updateOne(@NotNull final Long userId, final String userEmail, final String password,
                           final String userName, final Instant lastUpdatedAt) {
    Assert.notNull(userId, "The parameter 'userId' cannot be 'null'");
    try {
      Map<TableField<AppUserRecord, ?>, Object> fieldValuesMap = new HashMap<>();
      if (userEmail != null) {
        fieldValuesMap.put(APP_USER.USER_EMAIL, userEmail);
      }
      if (password != null) {
        fieldValuesMap.put(APP_USER.PASSWORD, password);
      }
      if (userName != null) {
        fieldValuesMap.put(APP_USER.USER_NAME, userName);
      }
      // Always update last_updated_at if provided, or let DB default to NOW() if not provided
      // If you want to force update from app, uncomment the line below
      // fieldValuesMap.put(APP_USER.LAST_UPDATED_AT, lastUpdatedAt != null ? lastUpdatedAt : Instant.now());

      int numberOfEntitiesUpdated = getDslContext().update(APP_USER)
              .set(fieldValuesMap)
              .where(APP_USER.USER_ID.eq(userId))
              .execute();
      return RepositoryUtils.validateSingleUpdate(numberOfEntitiesUpdated);
    } catch (RuntimeException re) {
      throw DatabaseAccessException.create(re);
    }
  }

  /**
   * Deletes an AppUserEntity by its ID.
   *
   * @param userId The ID of the user to delete. Must not be null.
   * @return true if exactly one entity was deleted, false otherwise.
   * @throws DatabaseAccessException if a database access error occurs.
   */
  @Transactional
  public boolean deleteOne(@NotNull final Long userId) {
    Assert.notNull(userId, "The parameter 'userId' cannot be 'null'");
    try {
      int numberOfEntitiesDeleted = getDslContext().deleteFrom(APP_USER)
              .where(APP_USER.USER_ID.eq(userId)).execute();
      return RepositoryUtils.validateSingleDelete(numberOfEntitiesDeleted);
    } catch (RuntimeException re) {
      throw DatabaseAccessException.create(re);
    }
  }

  public DSLContext getDslContext() {
    return dslContext;
  }
}
