package com.promptwise.promptchain.service;



public interface TelemetryProcessor<TelemetryRequest> {
  void process(com.promptwise.promptchain.dto.TelemetryRequest request);
}
