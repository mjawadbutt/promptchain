package com.promptwise.promptchain.dto;

import java.time.Instant;
import java.util.Map;

public record SpanDTO(
        String traceId,
        String spanId,
        String parentSpanId,
        String name,
        Instant startTime,
        Instant endTime,
        Map<String, String> attributes
) {}
