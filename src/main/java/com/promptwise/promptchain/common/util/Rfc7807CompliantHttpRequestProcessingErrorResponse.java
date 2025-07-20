package com.promptwise.promptchain.common.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.promptwise.promptchain.common.util.json.JacksonUtil;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.util.Assert;

import java.net.URI;
import java.util.Map;

public class Rfc7807CompliantHttpRequestProcessingErrorResponse {

  private final String errorCode;

  private final ProblemDetail problemDetail;

  private final Rfc7807CompliantHttpRequestProcessingErrorResponse cause;

  private Rfc7807CompliantHttpRequestProcessingErrorResponse(@NotNull final String errorCode,
                                                             @NotNull final ProblemDetail problemDetail,
                                                             final Rfc7807CompliantHttpRequestProcessingErrorResponse cause) {
    Assert.notNull(errorCode, "The 'errorCode' cannot be 'null'!");
    Assert.notNull(problemDetail, "The 'problemDetail' cannot be 'null'!");

    this.errorCode = errorCode;
    this.problemDetail = problemDetail;
    this.cause = cause;
  }

  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public static Rfc7807CompliantHttpRequestProcessingErrorResponse create(
          @NotNull final String errorCode, @NotNull final ProblemDetail problemDetail,
          final Rfc7807CompliantHttpRequestProcessingErrorResponse cause) {
    return new Rfc7807CompliantHttpRequestProcessingErrorResponse(errorCode, problemDetail, cause);
  }

  public static Rfc7807CompliantHttpRequestProcessingErrorResponse create(
          @NotNull final String errorCode, @NotNull final HttpStatus httpStatus, final String errorTitle,
          final String errorMessage, final URI errorInstanceUri,
          final Map<String, Object> problemDetailCustomPropertyMap,
          final Rfc7807CompliantHttpRequestProcessingErrorResponse cause) {
    ProblemDetail problemDetail = createProblemDetail(httpStatus, errorTitle, errorMessage, errorInstanceUri,
            problemDetailCustomPropertyMap);
    return new Rfc7807CompliantHttpRequestProcessingErrorResponse(errorCode, problemDetail, cause);
  }

  private static ProblemDetail createProblemDetail(HttpStatus httpStatus, String errorTitle,
                                                   String errorMessage, URI errorInstanceUri,
                                                   Map<String, Object> problemDetailCustomPropertyMap) {
    //-- The error's HTTP 'status' and 'detail' as per RFC-7807.
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, errorMessage);
    //-- The error's generic 'title' as per RFC-7807 (a generic summary about the error).
    problemDetail.setTitle(errorTitle);
    //-- 'Type' is an absolute URI (URL) that points to an HTTP link to a doc giving generic detail about the error
    //-- indicated by the generic description i.e. the 'title'
    //problemDetail.setType();
    //-- The request URI of the operation that caused the error.
    problemDetail.setInstance(errorInstanceUri);
    if (problemDetailCustomPropertyMap != null) {
      problemDetail.setProperties(Map.copyOf(problemDetailCustomPropertyMap));
    }
    return problemDetail;
  }

  public String toString() {
    return JacksonUtil.getInstance().serializeObjectToJsonString(this);
  }

  public String getErrorCode() {
    return errorCode;
  }

  public ProblemDetail getProblemDetail() {
    return problemDetail;
  }

  public Rfc7807CompliantHttpRequestProcessingErrorResponse getCause() {
    return cause;
  }

}

