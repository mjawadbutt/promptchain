package com.promptwise.promptchain.common.exception;

import org.springframework.core.annotation.Order;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * This class has been added as a workaround to the following bug:
 *
 * @see "https://github.com/spring-projects/spring-framework/issues/31569"
 * The effects us in the following way:
 * If the {@link ProblemDetail#getInstance()} property is properly populated with the URI that caused the error then
 * Spring tries retrieve it as a static resource which result in a 404 response sent to the caller, which hides the
 * actual error response. With this handler in place, the actual error response is sent to the caller.
 */
@ControllerAdvice
@Order(-1)
class NoResourceFoundExceptionHandler {

  @ExceptionHandler(NoResourceFoundException.class)
  public final ResponseEntity<Object> handleResourceNotFound(Exception ex) throws Exception {
    throw ex;
  }

}