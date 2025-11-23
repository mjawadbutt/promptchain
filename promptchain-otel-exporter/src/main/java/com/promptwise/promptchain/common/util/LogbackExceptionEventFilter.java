package com.promptwise.promptchain.common.util;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.boolex.EventEvaluatorBase;

public class LogbackExceptionEventFilter extends EventEvaluatorBase<ILoggingEvent> {

  public boolean evaluate(ILoggingEvent event) {
    IThrowableProxy throwableProxy = event.getThrowableProxy();
    return throwableProxy != null;
  }

}
