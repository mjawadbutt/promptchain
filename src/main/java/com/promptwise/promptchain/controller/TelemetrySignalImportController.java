package com.promptwise.promptchain.controller;

import com.promptwise.promptchain.model.LogSignals;
import com.promptwise.promptchain.model.MetricSignals;
import com.promptwise.promptchain.model.TraceSignals;
import com.promptwise.promptchain.service.LogSignalsImportService;
import com.promptwise.promptchain.service.MetricSignalImportService;
import com.promptwise.promptchain.service.TraceSignalsImportService;
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
  private final LogSignalsImportService logSignalsImportService;
  private final TraceSignalsImportService traceSignalsImportService;
  private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

  public TelemetrySignalImportController(@NotNull final MetricSignalImportService metricSignalImportService,
                                         @NotNull final LogSignalsImportService logSignalsImportService,
                                         @NotNull final TraceSignalsImportService traceSignalsImportService) {
    this.metricSignalImportService = metricSignalImportService;
    this.logSignalsImportService = logSignalsImportService;
    this.traceSignalsImportService = traceSignalsImportService;
  }

  @PostMapping(value = TelemetrySignalImportController.URI__V1 + "/metrics",
          consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> importMetricSignals(@RequestBody MetricSignals metricSignals) {
    getExecutorService().submit(() -> {
      //adding to Raw table (raw_metrics)
      getMetricSignalsProcessorService().importRawMetrics(metricSignals, 1L,1L);
      //processing the metrics
      getMetricSignalsProcessorService().processSignals(metricSignals);
    });
    return ResponseEntity.ok(Map.of());
  }

  @PostMapping(value = TelemetrySignalImportController.URI__V1 + "/logs",
          consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> importLogSignals(@RequestBody LogSignals logSignals) {
    getExecutorService().submit(() -> {
      getLogSignalsImportService().processSignals(logSignals);
    });
    return ResponseEntity.ok(Map.of());
  }

  @PostMapping(value = TelemetrySignalImportController.URI__V1 + "/traces",
          consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> importTraceSignals(@RequestBody TraceSignals traceSignals) {
    getExecutorService().submit(() -> {
      getTraceProcessorService().processSignals(traceSignals);
    });
    return ResponseEntity.ok(Map.of());
  }

  public MetricSignalImportService getMetricSignalsProcessorService() {
    return metricSignalImportService;
  }

  public LogSignalsImportService getLogSignalsImportService() {
    return logSignalsImportService;
  }

  public TraceSignalsImportService getTraceProcessorService() {
    return traceSignalsImportService;
  }

  public ExecutorService getExecutorService() {
    return executorService;
  }

}