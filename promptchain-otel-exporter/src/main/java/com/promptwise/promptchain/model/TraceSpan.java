package com.promptwise.promptchain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.promptwise.promptchain.common.util.json.JacksonUtil;
import io.opentelemetry.proto.trace.v1.Span;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Objects;

public class TraceSpan {

  private final String traceId;
  private final String spanId;
  private final String parentSpanId;
  private final String name;
  private final Span.SpanKind kind;
  private final String startTimeUnixNano;
  private final String endTimeUnixNano;
  private final List<SignalAttribute> attributes;
  private final SpanStatus status;
  private final List<TraceSpanEvent> events;

  public TraceSpan(final String traceId, final String spanId, final String parentSpanId, final String name,
                   final Span.SpanKind kind, final @NotNull String startTimeUnixNano, @NotNull final String endTimeUnixNano,
                   final List<SignalAttribute> attributes, final SpanStatus status, final List<TraceSpanEvent> events) {
    this.traceId = traceId;
    this.spanId = spanId;
    this.parentSpanId = parentSpanId;
    this.name = name;
    this.kind = kind;
    this.startTimeUnixNano = Objects.requireNonNull(startTimeUnixNano, "The parameter 'startTimeUnixNano' is required!");
    this.endTimeUnixNano = Objects.requireNonNull(endTimeUnixNano, "The parameter 'endTimeUnixNano' is required!");
    this.attributes = attributes == null ? List.of() : List.copyOf(attributes);
    this.status = status;
    this.events = events == null ? List.of() : List.copyOf(events);
  }

  @JsonIgnore
  public Long getStartTimeUnixNanoAsLong() {
    return Long.parseUnsignedLong(startTimeUnixNano);
  }

  @JsonIgnore
  public Long getEndTimeUnixNanoAsLong() {
    return Long.parseUnsignedLong(endTimeUnixNano);
  }

  public String getTraceId() {
    return traceId;
  }

  public String getSpanId() {
    return spanId;
  }

  public String getParentSpanId() {
    return parentSpanId;
  }

  public String getName() {
    return name;
  }

  public Span.SpanKind getKind() {
    return kind;
  }

  public String getStartTimeUnixNano() {
    return startTimeUnixNano;
  }

  public String getEndTimeUnixNano() {
    return endTimeUnixNano;
  }

  public List<SignalAttribute> getAttributes() {
    return attributes;
  }

  public SpanStatus getStatus() {
    return status;
  }

  public List<TraceSpanEvent> getEvents() {
    return events;
  }

  @Override
  public String toString() {
    return JacksonUtil.getInstance().serializeObjectToJsonString(this);
  }

}
