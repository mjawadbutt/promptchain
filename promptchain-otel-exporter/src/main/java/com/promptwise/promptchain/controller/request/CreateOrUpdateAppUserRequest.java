package com.promptwise.promptchain.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.promptwise.promptchain.entity.AppUserEntity;
import jakarta.validation.Valid;

public record CreateOrUpdateAppUserRequest(
        @JsonProperty("appUser") @Valid AppUserEntity appUserEntity) {
}