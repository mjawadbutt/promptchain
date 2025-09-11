package com.promptwise.promptchain.service;

import com.promptwise.promptchain.model.LogRecord;
import com.promptwise.promptchain.model.LogSignal;
import com.promptwise.promptchain.model.LogSignals;
import com.promptwise.promptchain.model.ScopeLogs;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LogSignalImportService implements TelemetrySignalImportService<LogSignal> {

  @Transactional
  public void importSignals(LogSignals logSignals) {
    for (LogSignal logSignal : logSignals.getLogSignals()) {
      importSignal(logSignal);
    }
  }

  @Transactional
  public void importSignal(LogSignal logSignal) {
    for (ScopeLogs scopeLogs : logSignal.getScopeSignals()) {
      for (LogRecord logRecord : scopeLogs.getSignals()) {
      }
    }
  }
}
