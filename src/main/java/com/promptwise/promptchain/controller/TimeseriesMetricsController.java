package com.promptwise.promptchain.controller;

import com.promptwise.promptchain.PromptChainApplication;
import com.promptwise.promptchain.repository.TimeseriesMetricsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(TimeseriesMetricsController.URI)
public class TimeseriesMetricsController {

  public static final String URI = PromptChainApplication.URI__API + "/times";

  private final TimeseriesMetricsRepository timeseriesMetricsRepository;

  public TimeseriesMetricsController(TimeseriesMetricsRepository timeseriesMetricsRepository) {
    this.timeseriesMetricsRepository = timeseriesMetricsRepository;
  }

  /**
   * Get all metrics from the last N minutes.
   */
  @GetMapping("/recent")
  public ResponseEntity<List<Map<String, Object>>> getRecentMetrics(
          @RequestParam(defaultValue = "15") int minutes) {
    List<Map<String, Object>> results = getTimeseriesMetricsRepository().getMetricsLastNMinutes(minutes);
    return ResponseEntity.ok(results);
  }

  /**
   * Get metrics filtered by service name and metric name.
   */
  @GetMapping("/filter")
  public ResponseEntity<List<Map<String, Object>>> getFilteredMetrics(
          @RequestParam String serviceName,
          @RequestParam String metricName,
          @RequestParam(defaultValue = "15") int minutes) {

    List<Map<String, Object>> results = getTimeseriesMetricsRepository().getMetricsByServiceAndName(
            serviceName, metricName, minutes);
    return ResponseEntity.ok(results);
  }

  public TimeseriesMetricsRepository getTimeseriesMetricsRepository() {
    return timeseriesMetricsRepository;
  }

}