package com.promptwise.promptchain.service;

import com.promptwise.promptchain.model.ScopeSignal;
import com.promptwise.promptchain.model.TelemetrySignal;

public interface TelemetrySignalImportService<T extends TelemetrySignal<? extends ScopeSignal<?>>> {

  void processSignal(T telemetrySignal);
}
