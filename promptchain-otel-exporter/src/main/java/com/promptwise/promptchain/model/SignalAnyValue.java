package com.promptwise.promptchain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.promptwise.promptchain.common.util.json.JacksonUtil;

import java.util.Objects;

//TODO: Maps to AnyValue in protobuf, Add support for kvlistValue and arrayValue values
public class SignalAnyValue {

  private final String stringValue;
  private final Long intValue;
  private final Double doubleValue;
  private final Boolean boolValue;

  public SignalAnyValue(
          @JsonProperty("stringValue") final String stringValue,
          @JsonProperty("intValue") final Long intValue,
          @JsonProperty("doubleValue") final Double doubleValue,
          @JsonProperty("boolValue") final Boolean boolValue) {
    this.stringValue = stringValue;
    this.intValue = intValue;
    this.doubleValue = doubleValue;
    this.boolValue = boolValue;
  }

  public String getStringValue() {
    return stringValue;
  }

  public Long getIntValue() {
    return intValue;
  }

  public Double getDoubleValue() {
    return doubleValue;
  }

  public Boolean getBoolValue() {
    return boolValue;
  }

  @Override
  public int hashCode() {
    return Objects.hash(stringValue, intValue, doubleValue, boolValue);
  }

  @Override
  public String toString() {
    return JacksonUtil.getInstance().serializeObjectToJsonString(this);
  }
}
