package com.promptwise.promptchain.controller;

import com.promptwise.promptchain.PromptChainApplication;
import com.promptwise.promptchain.dto.TelemetryRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This is the main OpenTelemetry Importer class to fetch
 * metrics, logs, traces
 */
@RestController
@RequestMapping(OtlpImporterRestController.WEB_CONTEXT)
public class OtlpImporterRestController {

  public static final String WEB_CONTEXT = PromptChainApplication.WEB_CONTEXT + "/api/telemetry";
  @PostMapping("/import")
  public ResponseEntity<String> importTelemetry(@RequestBody TelemetryRequest request) {

    request.spans().forEach(span -> {
      System.out.println("Received span: " + span.name() + " traceId=" + span.traceId());
    });


    request.metrics().forEach(metric -> {
      System.out.println("Received metric: " + metric.name() + " value=" + metric.value());
    });


    request.logs().forEach(log -> {
      System.out.println("Received log: " + log.message() + " severity=" + log.severity());
    });

    return ResponseEntity.status(HttpStatus.ACCEPTED).body("Telemetry Imported Successfully");
  }

}
