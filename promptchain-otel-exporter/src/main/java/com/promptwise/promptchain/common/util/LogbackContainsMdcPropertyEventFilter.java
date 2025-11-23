package com.promptwise.promptchain.common.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.boolex.EventEvaluatorBase;

public class LogbackContainsMdcPropertyEventFilter extends EventEvaluatorBase<ILoggingEvent> {

  private boolean start = false;
  private String mdcPropertyToCheck;

  public LogbackContainsMdcPropertyEventFilter() {
  }

  @Override
  public void start() {
    if (mdcPropertyToCheck == null) {
      addError("The mdcPropertyToCheck must be set");
      return;
    }
    start = true;
  }

  @Override
  public void stop() {
    start = false;
  }

  @Override
  public boolean isStarted() {
    return start;
  }

  @Override
  public boolean evaluate(ILoggingEvent event) {
    boolean filterResult = Level.ERROR == event.getLevel()
            && event.getMDCPropertyMap().containsKey(getMdcPropertyToCheck());
    return filterResult;
  }

  public String getMdcPropertyToCheck() {
    return mdcPropertyToCheck;
  }

  public void setMdcPropertyToCheck(String mdcPropertyToCheck) {
    this.mdcPropertyToCheck = mdcPropertyToCheck;
  }

}
