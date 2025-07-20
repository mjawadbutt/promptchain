package com.promptwise.promptchain;

public enum PromptChainErrorCode {

//  REST_CLIENT_ERROR("The rest-client was unable to invoke the end point due to an exception!",
//          HttpStatus.MULTI_STATUS),

  AUTHENTICATION_FAILED("Authentication failed due to invalid credentials!"),

  RESOURCE_ALREADY_EXISTS("The resource being created already exists!"),

  REQUIRED_RESOURCE_NOT_FOUND("A resource required to fulfill the request was not found!"),

  DATABASE_ACCESS_ERROR("A database error has occurred!"),

  INTERNAL_APPLICATION_ERROR("An internal application error has occurred!"),

  INTERNAL_SERVER_ERROR("An internal server error has occurred!"),

//  XWS_ENDPOINT_ACCESS_ERROR("The 'IFlow-Xws-Service' was rejected by the 'IFlowXwsControllerClient' because it was unable to access the requested resource!"),

  //-- This is the case when an exception occurs when the 'IFlowXwsControllerClient' tries to make a request to the
  //-- 'IFlow-Xws-Service'.
  IFLOW_XWS_CLIENT_ERROR("An exception occurred while invoking an 'IFlow-Xws-Service' endpoint!"),

  IFLOW_XWS_SERVER_ERROR("An exception occurred within the 'IFlow-Xws-Service' while it was processing a request!"),

  //-- This is the case when an IFlow controller rejects a request because it considers the request as being invalid
  //-- (i.e. the issue is with the client/caller of the endpoint and not the server.
  CLIENT_ERROR("An exception occurred while processing the request because because the request is not valid!"),

  SERVER_ERROR("An exception occurred while processing the request due to a server error!"),

  UNKNOWN_ERROR("TODO");

  /**
   * The error's generic 'title' as per RFC-7807.
   */
  private final String errorTitle;

  PromptChainErrorCode(String errorTitle) {
    this.errorTitle = errorTitle;
  }

  public String getErrorTitle() {
    return errorTitle;
  }

}
