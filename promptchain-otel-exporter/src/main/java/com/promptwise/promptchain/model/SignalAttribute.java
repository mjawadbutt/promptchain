package com.promptwise.promptchain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.promptwise.promptchain.common.util.json.JacksonUtil;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class SignalAttribute {

  private final String key;
  private final SignalAnyValue value;

  public SignalAttribute(
          @JsonProperty("key") @NotNull final String key,
          @JsonProperty("value") @NotNull final SignalAnyValue value) {
    this.key = Objects.requireNonNull(key, "key must not be null");
    this.value = Objects.requireNonNull(value, "value must not be null");
  }

  public String getKey() {
    return key;
  }

  public SignalAnyValue getValue() {
    return value;
  }

  @Override
  public int hashCode() {
    return Objects.hash(key, value);
  }

  @Override
  public String toString() {
    return JacksonUtil.getInstance().serializeObjectToJsonString(this);
  }
}
