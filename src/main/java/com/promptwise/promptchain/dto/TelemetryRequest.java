package com.promptwise.promptchain.dto;


import java.time.Instant;
import java.util.List;
import java.util.Map;

public record TelemetryRequest(
        List<SpanDTO> spans,
        List<MetricDTO> metrics,
        List<LogDTO> logs
) {}

