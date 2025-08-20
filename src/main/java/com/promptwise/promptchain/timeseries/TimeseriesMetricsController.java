package com.promptwise.promptchain.timeseries;

import com.promptwise.promptchain.PromptChainApplication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(TimeseriesMetricsController.WEB_CONTEXT)
public class TimeseriesMetricsController {

  public static final String WEB_CONTEXT = PromptChainApplication.WEB_CONTEXT + "/api/times";

  TimeseriesMetricsRepository repository;

  public TimeseriesMetricsController(TimeseriesMetricsRepository repository) {
    this.repository = repository;
  }

  @GetMapping(path = "/ping", produces = MediaType.TEXT_PLAIN_VALUE)
  public String ping() {
    return "success";
  }
  /**
   * Get all metrics from the last N minutes.
   */
  @GetMapping("/recent")
  public ResponseEntity<List<Map<String, Object>>> getRecentMetrics(
          @RequestParam(defaultValue = "15") int minutes) {
    List<Map<String, Object>> results = repository.getMetricsLastNMinutes(minutes);
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

    List<Map<String, Object>> results = repository.getMetricsByServiceAndName(serviceName, metricName, minutes);
    return ResponseEntity.ok(results);
  }

}