package com.promptwise.promptchain.dto;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record MetricDTO(
        String name,
        String unit,
        String serviceName,
        List<DataPoint> dataPoints
) {
  public record DataPoint(long timeUnixNano, Double value, Map<String, Object> labels) {}
}
