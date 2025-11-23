package com.promptwise.promptchain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.promptwise.promptchain.common.util.json.JacksonUtil;

import java.util.Objects;

public class ScopeSignalId {

  private final String name;
  private final String version;

  public ScopeSignalId(
          @JsonProperty("name") //-- Property name as per Standard OLTP JSON
          final String name,
          @JsonProperty("version") //-- Property name as per Standard OLTP JSON
          final String version) {
    this.name = Objects.requireNonNull(name, "name must not be null");
    this.version = Objects.requireNonNull(version, "version must not be null");
  }

  public String getName() {
    return name;
  }

  public String getVersion() {
    return version;
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, version);
  }

  @Override
  public String toString() {
    return JacksonUtil.getInstance().serializeObjectToJsonString(this);
  }
}
