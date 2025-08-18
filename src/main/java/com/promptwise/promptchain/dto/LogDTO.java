package com.promptwise.promptchain.dto;

import java.time.Instant;
import java.util.Map;

public record LogDTO(
        String severity,
        String message,
        Instant timestamp,
        Map<String, String> attributes
) {}
