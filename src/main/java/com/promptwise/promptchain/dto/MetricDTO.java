package com.promptwise.promptchain.dto;

import java.time.Instant;
import java.util.Map;

public record MetricDTO(
        String name,
        String type,  // e.g., counter, gauge, histogram
        double value,
        Instant timestamp,
        Map<String, String> labels
) {}
