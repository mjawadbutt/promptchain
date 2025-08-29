package com.promptwise.promptchain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.promptwise.promptchain.common.util.json.JacksonUtil;

public class SpanStatus {
  private final String code;
  private final String message;

  public SpanStatus(
          @JsonProperty("code") String code,
          @JsonProperty("message") String message) {
    this.code = code;
    this.message = message;
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public String toString() {
    return JacksonUtil.getInstance().serializeObjectToJsonString(this);
  }

}
