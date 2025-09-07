package com.promptwise.promptchain.controller;

import com.promptwise.promptchain.model.LogSignals;
import com.promptwise.promptchain.model.MetricSignals;
import com.promptwise.promptchain.model.TraceSignals;
import com.promptwise.promptchain.service.LogSignalImportService;
import com.promptwise.promptchain.service.MetricSignalImportService;
import com.promptwise.promptchain.service.TraceSignalImportService;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This is the main OpenTelemetry Importer class to fetch metrics, logs, traces
 */
@RestController
public class TelemetrySignalImportController {

  public static final String URI__V1 = "/v1";
  private static final Logger LOGGER = LoggerFactory.getLogger(TelemetrySignalImportController.class);

  private final MetricSignalImportService metricSignalImportService;
  private final LogSignalImportService logSignalImportService;
  private final TraceSignalImportService traceSignalImportService;
  private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

  public TelemetrySignalImportController(@NotNull final MetricSignalImportService metricSignalImportService,
                                         @NotNull final LogSignalImportService logSignalImportService,
                                         @NotNull final TraceSignalImportService traceSignalImportService) {
    this.metricSignalImportService = metricSignalImportService;
    this.logSignalImportService = logSignalImportService;
    this.traceSignalImportService = traceSignalImportService;
  }

  @PostMapping(value = TelemetrySignalImportController.URI__V1 + "/metrics",
          consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> importMetricSignals(@RequestBody MetricSignals metricSignals) {
    getExecutorService().submit(() -> {
      //adding to Raw table (raw_metrics)
      getMetricSignalsProcessorService().importRawMetrics(metricSignals, 1L,1L);
      //processing the metrics
      getMetricSignalsProcessorService().importSignals(metricSignals);
    });
    return ResponseEntity.ok(Map.of());
  }

  @PostMapping(value = TelemetrySignalImportController.URI__V1 + "/logs",
          consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> importLogSignals(@RequestBody LogSignals logSignals) {
    getExecutorService().submit(() -> {
      getLogSignalsImportService().importSignals(logSignals);
    });
    return ResponseEntity.ok(Map.of());
  }

  @PostMapping(value = TelemetrySignalImportController.URI__V1 + "/traces",
          consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> importTraceSignals(@RequestBody TraceSignals traceSignals) {
    getExecutorService().submit(() -> {
      getTraceProcessorService().importSignals(traceSignals);
    });
    return ResponseEntity.ok(Map.of());
  }

  public MetricSignalImportService getMetricSignalsProcessorService() {
    return metricSignalImportService;
  }

  public LogSignalImportService getLogSignalsImportService() {
    return logSignalImportService;
  }

  public TraceSignalImportService getTraceProcessorService() {
    return traceSignalImportService;
  }

  public ExecutorService getExecutorService() {
    return executorService;
  }

}