package com.promptwise.promptchain.controller;

import com.promptwise.promptchain.PromptChainApplication;
import com.promptwise.promptchain.dto.TelemetryRequest;
import com.promptwise.promptchain.service.LogProcessorService;
import com.promptwise.promptchain.service.MetricProcessorService;
import com.promptwise.promptchain.service.TraceProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This is the main OpenTelemetry Importer class to fetch
 * metrics, logs, traces
 */
@RestController
@RequestMapping(OtelCollectorRestController.WEB_CONTEXT)
public class OtelCollectorRestController {

  public static final String WEB_CONTEXT = PromptChainApplication.WEB_CONTEXT + "/api/telemetry";
  ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
  @Autowired
  MetricProcessorService metricProcessorService;
  @Autowired
  LogProcessorService logProcessorService;
  @Autowired
  //Traces = collection of spans.
  TraceProcessorService traceProcessorService;



  /**
   * This is the main importer for metrics, logs, traces
   * It would create a new virtual thread for every request
   * TODO: Also add support for gRPC
   * @param request
   * @return
   */
  @PostMapping("/import")
  public ResponseEntity<String> importTelemetry(@RequestBody TelemetryRequest request) {

   executorService.submit(()->{
     processTelemetry(request);
   });

    return ResponseEntity.status(HttpStatus.ACCEPTED).body("Telemetry Imported Successfully");
  }

  private void processTelemetry(TelemetryRequest request){

    if(request.spans()!=null) {
      request.spans().forEach(span -> {
        System.out.println("Received span: " + span.name() + " traceId=" + span.traceId());
        //Traces = collection of spans.
        traceProcessorService.process(request);

      });
    }

    if(request.metrics()!=null) {
      request.metrics().forEach(metric -> {
        System.out.println("Received metric: " + metric.name() + " value=" + metric.unit());
        metricProcessorService.process(request);
      });
    }

    if(request.logs()!=null) {
      request.logs().forEach(log -> {
        System.out.println("Received log: " + log.message() + " severity=" + log.severity());
        logProcessorService.process(request);
      });
    }
  }


}
