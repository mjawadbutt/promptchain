package com.promptwise.promptchain.service;

import com.promptwise.promptchain.model.LogRecord;
import com.promptwise.promptchain.model.LogSignal;
import com.promptwise.promptchain.model.LogSignals;
import com.promptwise.promptchain.model.ScopeLogs;
import com.promptwise.promptchain.repository.TimeseriesMetricsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogSignalsImportService implements TelemetrySignalImportService<LogSignal> {

  @Autowired
  TimeseriesMetricsRepository timeseriesMetricsRepository;

  public void processSignals(LogSignals logSignals) {
    for (LogSignal logSignal : logSignals.getLogSignals()) {
      processSignal(logSignal);
    }
  }

  public void processSignal(LogSignal logSignal) {
    for (ScopeLogs scopeLogs : logSignal.getScopeSignals()) {
      for (LogRecord logRecord : scopeLogs.getSignals()) {
      }
    }
  }
}
