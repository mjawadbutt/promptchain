package com.promptwise.promptchain.model;

import com.promptwise.promptchain.common.util.json.JacksonUtil;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Objects;

public class SignalResource {

  private final List<SignalAttribute> attributes;

  protected SignalResource(@NotNull final List<SignalAttribute> attributes) {
    this.attributes = attributes == null ? List.of() : List.copyOf(attributes);
  }

  public List<SignalAttribute> getAttributes() {
    return attributes;
  }

  @Override
  public int hashCode() {
    return Objects.hash(attributes);
  }

  @Override
  public String toString() {
    return JacksonUtil.getInstance().serializeObjectToJsonString(this);
  }
}
