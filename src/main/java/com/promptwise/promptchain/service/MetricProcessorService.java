package com.promptwise.promptchain.service;

import com.promptwise.promptchain.dto.MetricDTO;
import com.promptwise.promptchain.dto.TelemetryRequest;
import com.promptwise.promptchain.timeseries.TimeseriesMetricsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
public class MetricProcessorService implements TelemetryProcessor {

  @Autowired
  TimeseriesMetricsRepository timeseriesMetricsRepository;

  @Override
  public void process(final TelemetryRequest request) {
    if (request.metrics() == null || request.metrics().isEmpty()) {
      return; // Nothing to process
    }

    for (MetricDTO metric : request.metrics()) {
      String serviceName = metric.serviceName(); // Or get from request/resource if needed
      String metricName = metric.name();
      String unit = metric.unit();

      if (metric.dataPoints() == null || metric.dataPoints().isEmpty()) {
        continue;
      }

      for (MetricDTO.DataPoint dp : metric.dataPoints()) {
        // Convert nanoseconds to Instant
        Instant timestamp = Instant.ofEpochMilli(dp.timeUnixNano() / 1_000_000);

        Double value = dp.value(); // numeric value
        Map<String, Object> labels = dp.labels() != null ? dp.labels() : Map.of();

        // Insert into TimescaleDB
        timeseriesMetricsRepository.insertMetric(
                timestamp,
                serviceName,
                metricName,
                value,
                unit,
                labels
        );
      }
    }
  }

}
