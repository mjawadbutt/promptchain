package com.promptwise.promptchain.service;

import com.promptwise.promptchain.model.ScopeTraces;
import com.promptwise.promptchain.model.TraceSignal;
import com.promptwise.promptchain.model.TraceSignals;
import com.promptwise.promptchain.model.TraceSpan;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class TraceSignalImportService implements TelemetrySignalImportService<TraceSignal> {

  @Transactional
  public void importSignals(TraceSignals traceSignals) {
    for (TraceSignal traceSignal : traceSignals.getTraceSignals()) {
      importSignal(traceSignal);
    }
  }

  @Transactional
  public void importSignal(TraceSignal traceSignal) {
    for (ScopeTraces scopeTraces : traceSignal.getScopeSignals()) {
      for (TraceSpan traceSpan : scopeTraces.getSignals()) {
      }
    }
  }
}