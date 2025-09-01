package com.promptwise.promptchain.model;

import com.promptwise.promptchain.common.util.json.JacksonUtil;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Objects;

public class LogRecord {

  private final String timeUnixNano;
  private final SeverityNumber severityNumber;
  private final String severityText;
  private final SignalAnyValue body;
  private final List<SignalAttribute> attributes;
  private final String traceId;
  private final String spanId;

  public LogRecord(@NotNull final String timeUnixNano,
                   final String severityText,
                   final SeverityNumber severityNumber,
                   final SignalAnyValue body,
                   final List<SignalAttribute> attributes,
                   final String traceId,
                   final String spanId) {
    this.timeUnixNano = Objects.requireNonNull(timeUnixNano, "timeUnixNano must not be null");
    this.severityText = severityText;
    this.severityNumber = severityNumber;
    this.body = body;
    this.attributes = attributes == null ? List.of() : List.copyOf(attributes);
    this.traceId = traceId;
    this.spanId = spanId;
  }

  public String getTimeUnixNano() {
    return timeUnixNano;
  }

  public SeverityNumber getSeverityNumber() {
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

  public String getTraceId() {
    return traceId;
  }

  public String getSpanId() {
    return spanId;
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
