package com.promptwise.promptchain.service;

import com.promptwise.promptchain.model.ScopeTraces;
import com.promptwise.promptchain.model.TraceSignal;
import com.promptwise.promptchain.model.TraceSignals;
import com.promptwise.promptchain.model.TraceSpan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TraceSignalsImportService implements TelemetrySignalImportService<TraceSignal> {



  public void processSignals(TraceSignals traceSignals) {
    for (TraceSignal traceSignal : traceSignals.getTraceSignals()) {
      processSignal(traceSignal);
    }
  }

  public void processSignal(TraceSignal traceSignal) {
    for (ScopeTraces scopeTraces : traceSignal.getScopeSignals()) {
      for (TraceSpan traceSpan : scopeTraces.getSignals()) {
      }
    }
  }
}