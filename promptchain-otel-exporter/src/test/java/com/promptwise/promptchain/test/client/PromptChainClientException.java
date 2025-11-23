package com.promptwise.promptchain.test.client;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class PromptChainClientException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 1L;

  private final HttpMethod httpMethod;

  private final String uriAsString;

  private final HttpStatus httpStatus;

  public PromptChainClientException(String uriAsString, String errorMessageOfTheCause, HttpMethod httpMethod,
                                    HttpStatus httpStatusOfCause, Throwable cause) {
    super(String.format("Unable to access the requested resource: '%s'! Reason: %s",
            uriAsString, errorMessageOfTheCause), cause);
    this.httpMethod = httpMethod;
    this.uriAsString = uriAsString;
    this.httpStatus = httpStatusOfCause;
  }

  public HttpMethod getHttpMethod() {
    return httpMethod;
  }

  public String getUriAsString() {
    return uriAsString;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

}
