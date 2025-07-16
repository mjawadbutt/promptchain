package com.promptwise.promptchain.exception;

import com.promptwise.promptchain.common.exception.DatabaseAccessException;
import com.promptwise.promptchain.common.exception.RequiredResourceNotFoundException;
import com.promptwise.promptchain.common.exception.ResourceAlreadyExistsException;
import com.promptwise.promptchain.common.util.Rfc7807CompliantHttpRequestProcessingErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@ControllerAdvice
public class PromptChainExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(PromptChainExceptionHandler.class);

  @Autowired
  public PromptChainExceptionHandler() {
  }

  @ExceptionHandler(RequiredResourceNotFoundException.class)
  public ResponseEntity<Rfc7807CompliantHttpRequestProcessingErrorResponse> handleIFlowRequiredResourceNotFoundException(
          RequiredResourceNotFoundException ex, HttpMethod httpMethod, HttpServletRequest httpServletRequest) {
    //-- Should not log caller errors, only respond. Logging should be done only for those exceptions that are
    //-- not because of the caller.
    //LOGGER.error(ex.getMessage(), ex);
    Rfc7807CompliantHttpRequestProcessingErrorResponse errorResponse = createErrorResponse(
            PromptChainErrorCode.REQUIRED_RESOURCE_NOT_FOUND.name(), HttpStatus.NOT_FOUND,
            PromptChainErrorCode.REQUIRED_RESOURCE_NOT_FOUND.getErrorTitle(),
            ex.getMessage(), httpServletRequest, null, null);
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ResourceAlreadyExistsException.class)
  public ResponseEntity<Rfc7807CompliantHttpRequestProcessingErrorResponse> handleIFlowResourceAlreadyExistsException(
          ResourceAlreadyExistsException ex, HttpMethod httpMethod, HttpServletRequest httpServletRequest) {
    //-- Should not log caller errors, only respond. Logging should be done only for those exceptions that are
    //-- not because of the caller.
    //LOGGER.error(ex.getMessage(), ex);
    Rfc7807CompliantHttpRequestProcessingErrorResponse errorResponse = createErrorResponse(
            PromptChainErrorCode.RESOURCE_ALREADY_EXISTS.name(), HttpStatus.CONFLICT,
            PromptChainErrorCode.RESOURCE_ALREADY_EXISTS.getErrorTitle(),
            ex.getMessage(), httpServletRequest, null, null);
    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(DatabaseAccessException.class)
  public ResponseEntity<Rfc7807CompliantHttpRequestProcessingErrorResponse> handleIFlowDatabaseAccessException(
          DatabaseAccessException ex, HttpMethod httpMethod, HttpServletRequest httpServletRequest) {
    LOGGER.error(ex.getMessage(), ex);
    Rfc7807CompliantHttpRequestProcessingErrorResponse errorResponse = createErrorResponse(
            PromptChainErrorCode.DATABASE_ACCESS_ERROR.name(), HttpStatus.INTERNAL_SERVER_ERROR,
            PromptChainErrorCode.DATABASE_ACCESS_ERROR.getErrorTitle(), ex.getMessage(), httpServletRequest,
            null, null);
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  //TODO-Exceptions: Handle Exception.class also?
  @ExceptionHandler({PromptChainSystemException.class, Exception.class})
  public ResponseEntity<Rfc7807CompliantHttpRequestProcessingErrorResponse> handleIFlowSystemException(
          PromptChainSystemException ex, HttpMethod httpMethod, HttpServletRequest httpServletRequest) {
    LOGGER.error(ex.getMessage(), ex);
    Rfc7807CompliantHttpRequestProcessingErrorResponse errorResponse = createErrorResponse(
            PromptChainErrorCode.INTERNAL_APPLICATION_ERROR.name(), HttpStatus.INTERNAL_SERVER_ERROR,
            PromptChainErrorCode.INTERNAL_APPLICATION_ERROR.getErrorTitle(), ex.getMessage(), httpServletRequest,
            null, null);
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
  //TODO-Exceptions: In case a requestbody param of a controller annotation (or any param of any method) has @Valid
  //TODO-Exceptions: annotation then if it has any validation constraints like @NotNull then they will be applied at
  //TODO-Exceptions: runtime and a org.springframework.web.bind.MethodArgumentNotValidException will be thrown if its
  //TODO-Exceptions: not valid. Currently this exception is handled generically by the handleExceptionInternal, which
  //TODO-Exceptions: ignores all the error details in the exception and just give us "Invalid request content".
  //TODO-Exceptions: In  case exact error details are needed then we can write a special handler for it in future.

  //TODO: some code maybe duplicate. also refactor so that every call comes to an overloaded handleInternal method.
  @Override
  protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                           HttpStatusCode statusCode, WebRequest request) {
    ResponseEntity<Object> responseEntity = super.handleExceptionInternal(ex, body, headers, statusCode, request);
    if (responseEntity == null) {
      return null;
    } else {
      PromptChainErrorCode promptChainErrorCode;
      if (statusCode.is4xxClientError()) {
        promptChainErrorCode = PromptChainErrorCode.CLIENT_ERROR;
      } else {
        promptChainErrorCode = PromptChainErrorCode.SERVER_ERROR;
        LOGGER.error(ex.getMessage(), ex);
      }
      HttpStatus errorHttpStatus = HttpStatus.valueOf(statusCode.value());
      URI instance = createRequestUriFromWebRequest(request);
      Rfc7807CompliantHttpRequestProcessingErrorResponse errorResponse;
      if (responseEntity.getBody() instanceof ProblemDetail) {
        ProblemDetail problemDetail = (ProblemDetail) responseEntity.getBody();
        if (problemDetail.getInstance() == null) {
          problemDetail.setInstance(instance);
        }
        if (StringUtils.isBlank(problemDetail.getTitle())) {
          problemDetail.setTitle(promptChainErrorCode.getErrorTitle());
        }
        errorResponse = Rfc7807CompliantHttpRequestProcessingErrorResponse.create(promptChainErrorCode.name(), problemDetail, null);
      } else {
        errorResponse = createErrorResponse(promptChainErrorCode.name(), errorHttpStatus,
                promptChainErrorCode.getErrorTitle(), ex.getMessage(), request, null);
      }
      return new ResponseEntity<>(errorResponse, errorHttpStatus);
    }
  }

  //TODO-Common-lib:
  static Rfc7807CompliantHttpRequestProcessingErrorResponse createErrorResponse(
          String errorCode, HttpStatus errorHttpStatus, String errorTitle, String errorMessage, WebRequest webRequest,
          Map<String, Object> problemDetailCustomPropertyMap) {
    //-- A servlet-context relative URI for the request that caused the error.
    URI errorInstanceUri = createRequestUriFromWebRequest(webRequest);
    return Rfc7807CompliantHttpRequestProcessingErrorResponse.create(errorCode,
            errorHttpStatus, errorTitle, errorMessage, errorInstanceUri,
            problemDetailCustomPropertyMap, null);
  }

  //TODO-Common-lib:
  static Rfc7807CompliantHttpRequestProcessingErrorResponse createErrorResponse(
          String errorCode, HttpStatus errorHttpStatus, String errorTitle, String errorMessage,
          HttpServletRequest httpServletRequest, Map<String, Object> problemDetailCustomPropertyMap,
          Rfc7807CompliantHttpRequestProcessingErrorResponse cause) {
    URI errorInstanceUri = createRequestUriFromHttpServletRequest(httpServletRequest);
    return createErrorResponse(errorCode, errorHttpStatus, errorTitle, errorMessage, errorInstanceUri,
            problemDetailCustomPropertyMap, cause);
  }

  static Rfc7807CompliantHttpRequestProcessingErrorResponse createErrorResponse(
          String errorCode, HttpStatus errorHttpStatus, String errorTitle, String errorMessage, URI uri,
          Map<String, Object> problemDetailCustomPropertyMap,
          Rfc7807CompliantHttpRequestProcessingErrorResponse cause) {
    Rfc7807CompliantHttpRequestProcessingErrorResponse errorResponse =
            Rfc7807CompliantHttpRequestProcessingErrorResponse.create(errorCode, errorHttpStatus,
                    errorTitle, errorMessage, uri, problemDetailCustomPropertyMap, cause);
    return errorResponse;
  }

  //TODO-Common-lib:
  private static URI createRequestUriFromHttpServletRequest(HttpServletRequest httpServletRequest) {
    String strRelativeRequestUriWithQueryParams = httpServletRequest.getRequestURI()
            + (StringUtils.isBlank(httpServletRequest.getQueryString()) ? "" : "?" + httpServletRequest.getQueryString());
    //-- A servlet-context relative URI for the request that caused the error.
    URI uri = URI.create(strRelativeRequestUriWithQueryParams);
    return uri;
  }

  //TODO-Common-lib:
  private static URI createRequestUriFromWebRequest(WebRequest webRequest) {
    String queryString = webRequest.getParameterMap().entrySet().stream()
            .map(p -> p.getKey() + "=" + String.join(",", p.getValue()))
            .reduce((p1, p2) -> p1 + "&" + p2)
            .orElse("");
    String strServletContextRelativeRequestPath = ServletUriComponentsBuilder.fromCurrentRequestUri().build().getPath();
    String strServletContextRelativeRequestUri = strServletContextRelativeRequestPath
            + (queryString.isEmpty() ? "" : "?" + queryString);
    //-- A servlet-context relative URI for the request that caused the error.
    URI uri = URI.create(strServletContextRelativeRequestUri);
    return uri;
  }

}