package com.promptwise.promptchain.model;

import com.promptwise.promptchain.common.util.json.JacksonUtil;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Objects;

public class LogRecord {

  private final String timeUnixNano;
  private final Integer severityNumber;
  private final String severityText;
  private final SignalAnyValue body;
  private final List<SignalAttribute> attributes;

  public LogRecord(@NotNull final String timeUnixNano,
                   final String severityText,
                   final Integer severityNumber,
                   final SignalAnyValue body,
                   final List<SignalAttribute> attributes) {
    this.timeUnixNano = Objects.requireNonNull(timeUnixNano, "timeUnixNano must not be null");
    this.severityText = severityText;
    this.severityNumber = severityNumber;
    this.body = body;
    this.attributes = attributes == null ? List.of() : List.copyOf(attributes);
  }

  public String getTimeUnixNano() {
    return timeUnixNano;
  }

  public Integer getSeverityNumber() {
    return severityNumber;
  }

  public String getSeverityText() {
    return severityText;
  }

  public SignalAnyValue getBody() {
    return body;
  }

  public List<SignalAttribute> getAttributes() {
    return attributes;
  }

  @Override
  public int hashCode() {
    return Objects.hash(timeUnixNano, severityText, severityNumber, body, attributes);
  }

  @Override
  public String toString() {
    return JacksonUtil.getInstance().serializeObjectToJsonString(this);
  }

}
