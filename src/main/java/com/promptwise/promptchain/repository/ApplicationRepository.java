package com.promptwise.promptchain.repository;

import jakarta.validation.constraints.NotNull;
import net.fbdms.iflow.iflowservice.entity.ApplicationEntity;
import net.fbdms.iflow.iflowservice.exception.IFlowDatabaseAccessException;
import net.fbdms.iflow.iflowservice.exception.IFlowRequiredResourceNotFoundException;
import net.fbdms.iflow.iflowservice.generated.jooq.tables.Application;
import net.fbdms.iflow.iflowservice.generated.jooq.tables.records.ApplicationRecord;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static net.fbdms.iflow.iflowservice.generated.jooq.Tables.*;
import static org.jooq.impl.DSL.*;

@Repository
@Transactional(readOnly = true)
public class ApplicationRepository {

  private final DSLContext dslContext;

  @Autowired
  public ApplicationRepository(@NotNull final DSLContext dslContext) {
    this.dslContext = dslContext;
  }

  @Transactional
  public ApplicationEntity insertOne(@NotNull final ApplicationEntity applicationEntity) {
    Assert.notNull(applicationEntity, "The parameter 'applicationEntity' cannot be 'null'");
    try {
      ApplicationRecord r = getDslContext().insertInto(APPLICATION)
              .set(APPLICATION.CLIENT_ID, applicationEntity.getClientId())
              .set(APPLICATION.PWS_JOB_CODE, applicationEntity.getPwsJobCode())
              .set(APPLICATION.PWS_USER_ID, applicationEntity.getPwsUserId())
              .set(APPLICATION.PWS_USER_PWD, applicationEntity.getPwsUserPwd())
              .set(APPLICATION.PWS_USER_PWD_IS_PLAINTEXT, applicationEntity.getPwsUserPwdIsPlaintext())
              .set(APPLICATION.PWS_SESSION_TIMEOUT_SECONDS, applicationEntity.getPwsSessionTimeoutSeconds())
              .set(APPLICATION.IWS_JOB_CODE, applicationEntity.getIwsJobCode())
              .set(APPLICATION.IWS_USER_ID, applicationEntity.getIwsUserId())
              .set(APPLICATION.IWS_USER_PWD, applicationEntity.getIwsUserPwd())
              .set(APPLICATION.IWS_USER_PWD_IS_PLAINTEXT, applicationEntity.getIwsUserPwdIsPlaintext())
              .set(APPLICATION.IWS_SESSION_TIMEOUT_SECONDS, applicationEntity.getIwsSessionTimeoutSeconds())
              .set(APPLICATION.TWS_JOB_CODE, applicationEntity.getTwsJobCode())
              .set(APPLICATION.TWS_USER_ID, applicationEntity.getTwsUserId())
              .set(APPLICATION.TWS_USER_PWD, applicationEntity.getTwsUserPwd())
              .set(APPLICATION.TWS_USER_PWD_IS_PLAINTEXT, applicationEntity.getTwsUserPwdIsPlaintext())
              .set(APPLICATION.TWS_SESSION_TIMEOUT_SECONDS, applicationEntity.getTwsSessionTimeoutSeconds())
              .set(APPLICATION.XWS_BASE_URL, applicationEntity.getXwsBaseUrl())
              .returning(APPLICATION.APPLICATION_ID)  // Ask DB to return the ID
              .fetchSingle();
      return selectOneRequired(r.getApplicationId());
    } catch (IFlowRequiredResourceNotFoundException | RuntimeException e) {
      throw IFlowDatabaseAccessException.create(e);
    }
  }

  public Set<ApplicationEntity> selectAll() {
    try {
      Set<ApplicationEntity> entitySet = getDslContext().select().from(APPLICATION)
              .fetch().into(ApplicationEntity.class).parallelStream().collect(Collectors.toSet());
      return entitySet;
    } catch (RuntimeException re) {
      throw IFlowDatabaseAccessException.create(re);
    }
  }

  public @NotNull ApplicationEntity selectOneRequired(@NotNull final Integer applicationId)
          throws IFlowRequiredResourceNotFoundException {
    Optional<ApplicationEntity> optionalApplicationEntity = selectOne(applicationId);
    return optionalApplicationEntity.orElseThrow(
            () -> IFlowRequiredResourceNotFoundException.createForApplicationEntity(applicationId));
  }

  public Optional<ApplicationEntity> selectOne(@NotNull final Integer applicationId) {
    Assert.notNull(applicationId, "The parameter 'applicationId' cannot be 'null'");
    try {
      Optional<ApplicationEntity> optionalApplicationEntity = getDslContext().select().from(APPLICATION)
              .where(APPLICATION.APPLICATION_ID.eq(applicationId))
              .fetchOptionalInto(ApplicationEntity.class);
      return optionalApplicationEntity;
    } catch (RuntimeException re) {
      throw IFlowDatabaseAccessException.create(re);
    }
  }

  public Optional<ApplicationEntity> selectOneByTwsJobCode(final String twsJobCode) {
    Assert.notNull(twsJobCode, "The parameter 'twsJobCode' cannot be 'null'");
    try {
      Optional<ApplicationEntity> optionalApplicationEntity = getDslContext().select().from(APPLICATION)
              .where(APPLICATION.TWS_JOB_CODE.eq(twsJobCode))
              .fetchOptionalInto(ApplicationEntity.class);
      return optionalApplicationEntity;
    } catch (RuntimeException re) {
      throw IFlowDatabaseAccessException.create(re);
    }
  }

  public Set<ApplicationEntity> selectApplicationsForClientHavingAtleastOneWorkflow(@NotNull final Integer clientId) {
    Assert.notNull(clientId, "The parameter 'clientId' cannot be 'null'");
    try {
      final Set<ApplicationEntity> entitySet = getDslContext().select().from(APPLICATION)
              .where(APPLICATION.CLIENT_ID.eq(clientId))
              .and(APPLICATION.APPLICATION_ID.in(
                      select(APPLICATION_WORKFLOW.APPLICATION_ID)
                              .from(APPLICATION_WORKFLOW)))
              .fetch().into(ApplicationEntity.class).parallelStream().collect(Collectors.toSet());
      return entitySet;
    } catch (Exception re) {
      throw IFlowDatabaseAccessException.create(re);
    }
  }

  @Transactional
  public boolean updateOne(@NotNull final Integer applicationId, final Integer clientId, final String pwsJobCode,
                           final String pwsUserId, final String pwsUserPwd, final Boolean pwsUserPwdIsPlaintext,
                           final Integer pwsSessionTimeoutSeconds, final String iwsJobCode, final String iwsUserId,
                           final String iwsUserPwd, final Boolean iwsUserPwdIsPlaintext,
                           final Integer iwsSessionTimeoutSeconds, final String twsJobCode, final String twsUserId,
                           final String twsUserPwd, final Boolean twsUserPwdIsPlaintext,
                           final Integer twsSessionTimeoutSeconds, final String xwsBaseUrl) {
    Assert.notNull(applicationId, "The parameter 'applicationId' cannot be 'null'");
    try {
      Map<TableField<ApplicationRecord, ?>, Object> fieldValuesMap = new HashMap<>();
      if (clientId != null) {
        fieldValuesMap.put(Application.APPLICATION.CLIENT_ID, clientId);
      }
      if (pwsJobCode != null) {
        fieldValuesMap.put(Application.APPLICATION.PWS_JOB_CODE, pwsJobCode);
      }
      if (pwsUserId != null) {
        fieldValuesMap.put(Application.APPLICATION.PWS_USER_ID, pwsUserId);
      }
      if (pwsUserPwd != null) {
        fieldValuesMap.put(Application.APPLICATION.PWS_USER_PWD, pwsUserPwd);
      }
      if (pwsUserPwdIsPlaintext != null) {
        fieldValuesMap.put(Application.APPLICATION.PWS_USER_PWD_IS_PLAINTEXT, pwsUserPwdIsPlaintext);
      }
      if (pwsSessionTimeoutSeconds != null) {
        fieldValuesMap.put(Application.APPLICATION.PWS_SESSION_TIMEOUT_SECONDS, pwsSessionTimeoutSeconds);
      }
      if (iwsJobCode != null) {
        fieldValuesMap.put(Application.APPLICATION.IWS_JOB_CODE, iwsJobCode);
      }
      if (iwsUserId != null) {
        fieldValuesMap.put(Application.APPLICATION.IWS_USER_ID, iwsUserId);
      }
      if (iwsUserPwd != null) {
        fieldValuesMap.put(Application.APPLICATION.IWS_USER_PWD, iwsUserPwd);
      }
      if (iwsUserPwdIsPlaintext != null) {
        fieldValuesMap.put(Application.APPLICATION.IWS_USER_PWD_IS_PLAINTEXT, iwsUserPwdIsPlaintext);
      }
      if (iwsSessionTimeoutSeconds != null) {
        fieldValuesMap.put(Application.APPLICATION.IWS_SESSION_TIMEOUT_SECONDS, iwsSessionTimeoutSeconds);
      }
      if (twsJobCode != null) {
        fieldValuesMap.put(Application.APPLICATION.TWS_JOB_CODE, twsJobCode);
      }
      if (twsUserId != null) {
        fieldValuesMap.put(Application.APPLICATION.TWS_USER_ID, twsUserId);
      }
      if (twsUserPwd != null) {
        fieldValuesMap.put(Application.APPLICATION.TWS_USER_PWD, twsUserPwd);
      }
      if (twsUserPwdIsPlaintext != null) {
        fieldValuesMap.put(Application.APPLICATION.TWS_USER_PWD_IS_PLAINTEXT, twsUserPwdIsPlaintext);
      }
      if (twsSessionTimeoutSeconds != null) {
        fieldValuesMap.put(Application.APPLICATION.TWS_SESSION_TIMEOUT_SECONDS, twsSessionTimeoutSeconds);
      }
      if (xwsBaseUrl != null) {
        fieldValuesMap.put(Application.APPLICATION.XWS_BASE_URL, xwsBaseUrl);
      }
      int numberOfEntitiesUpdated = getDslContext().update(APPLICATION)
              .set(fieldValuesMap)
              .where(APPLICATION.APPLICATION_ID.eq(applicationId))
              .execute();
      return RepositoryUtils.validateSingleUpdate(numberOfEntitiesUpdated);
    } catch (Exception re) {
      throw IFlowDatabaseAccessException.create(re);
    }
  }

  @Transactional
  public boolean deleteOne(@NotNull final Integer applicationId) {
    Assert.notNull(applicationId, "The parameter 'applicationId' cannot be 'null'");
    try {
      int numberOfEntitiesDeleted = getDslContext().deleteFrom(APPLICATION)
              .where(APPLICATION.APPLICATION_ID.eq(applicationId)).execute();
      return RepositoryUtils.validateSingleDelete(numberOfEntitiesDeleted);
    } catch (RuntimeException re) {
      throw IFlowDatabaseAccessException.create(re);
    }
  }

  public DSLContext getDslContext() {
    return dslContext;
  }

}
