package com.promptwise.promptchain.service;

import com.promptwise.promptchain.model.LogRecord;
import com.promptwise.promptchain.model.LogSignal;
import com.promptwise.promptchain.model.LogSignals;
import com.promptwise.promptchain.model.ScopeLogs;
import org.springframework.stereotype.Service;

@Service
public class LogSignalsImportService implements TelemetrySignalImportService<LogSignal> {


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
