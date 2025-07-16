package com.promptwise.promptchain.common.exception;

import java.io.Serial;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class UnsupportedApiException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 1L;

  public static UnsupportedApiException create(String operationId,
                                               LinkedHashMap<String, Object> parameterNameToValueMap,
                                               String detailedErrorMessage, UnsupportedOperationException cause) {
    String fullErrorMessage = String.format("""
            The requested operation: '%s' has not been implemented yet! The detailed error message is: '%s'
            """, operationId, detailedErrorMessage);
    return new UnsupportedApiException(operationId, parameterNameToValueMap, fullErrorMessage, cause);
  }

  private UnsupportedApiException(String operationId, LinkedHashMap<String, Object> parameterNameToValueMap,
                                  String fullErrorMessage, UnsupportedOperationException cause) {
    super(fullErrorMessage, cause);
  }

  private static String getOperationInvocationDetailAsString(String apiName,
                                                             Map<String, Object> parameterNameToValueMap) {
    String parameters = parameterNameToValueMap == null ? "" : parameterNameToValueMap.entrySet().
            stream().map(entry -> entry.getKey() + ": '" + entry.getValue() + "'").collect(Collectors.joining(", "));
    String operationInvocationDetailAsString = String.format("%s(%s)", apiName, parameters);
    return operationInvocationDetailAsString;
  }

}
