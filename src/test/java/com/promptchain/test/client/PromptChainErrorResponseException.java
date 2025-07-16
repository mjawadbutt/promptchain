package com.promptchain.test.client;

import com.promptwise.promptchain.common.util.Rfc7807CompliantHttpRequestProcessingErrorResponse;

import java.io.Serial;

public class PromptChainErrorResponseException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 1L;
  private final Rfc7807CompliantHttpRequestProcessingErrorResponse errorResponse;

  public PromptChainErrorResponseException(Rfc7807CompliantHttpRequestProcessingErrorResponse errorResponse) {
    super(errorResponse.toString());
    this.errorResponse = errorResponse;
  }

  public Rfc7807CompliantHttpRequestProcessingErrorResponse getErrorResponse() {
    return errorResponse;
  }

}
