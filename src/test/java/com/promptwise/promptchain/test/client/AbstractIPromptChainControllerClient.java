package com.promptwise.promptchain.test.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.function.Function;

abstract class AbstractIPromptChainControllerClient {

  private final String baseUrl;
  private final ObjectMapper objectMapper;
  private final RestClient restClient;

  protected AbstractIPromptChainControllerClient(@NotNull final String baseUrl, @NotNull final ObjectMapper objectMapper) {
    Assert.notNull(objectMapper, "The objectMapper 'clientId' cannot be 'null'!");
    this.baseUrl = baseUrl;
    this.objectMapper = objectMapper;
    this.restClient = RestClient.create(baseUrl);
  }

  protected <T> T invokeRequest(HttpMethod httpMethod, String path, MediaType contentType,
                                HttpHeaders additionalHeaders, MultiValueMap<String, String> params,
                                MediaType acceptedMediaType, Function<RestClient.RequestBodySpec, T> requestInvoker) {
    try {
      RestClient.RequestBodyUriSpec uriSpec = getRestClient().method(httpMethod);
      HttpHeaders httpHeaders;
      if (additionalHeaders == null) {
        httpHeaders = new HttpHeaders();
      } else {
        httpHeaders = additionalHeaders;
      }
      httpHeaders.setContentType(contentType);
      uriSpec.headers(headers -> headers.addAll(httpHeaders));
      UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(getBaseUrl() + path);
      UriComponents uriComponents = uriComponentsBuilder.queryParams(params).build();
      URI uri;
      try {
        uri = uriComponents.toUri();
        uri.toURL();
      } catch (MalformedURLException | RuntimeException e) {
        throw new PromptChainClientException(uriComponents.toUriString(),
                String.format("'%s' (PROBABLE CAUSE: The specified baseUrl: '%s' is malformed or has syntax errors)",
                        getBaseUrl(), e.getMessage()), httpMethod, HttpStatus.BAD_REQUEST, e);
      }
      RestClient.RequestBodySpec bodySpec = uriSpec.uri(uri);
      bodySpec.accept(acceptedMediaType);
      try {
        //-- The below call invokes Spring's RestClient, which can cause two basic types of errors/exceptions. First
        //-- one would be for the case when the call did not even reach the server (this would happen in cases like
        //-- invalid url, incorrect port, service not running etc). The second one would be for the case when the call
        //-- did reach the server but the service returned an error response (either due to client OR server error).
        //-- The ResourceAccessException, RestClientException, IFlowClientException, and RuntimeException being
        //-- caught below refer to the first case. And the IFlowErrorResponseException refers to the second case.
        T response = requestInvoker.apply(bodySpec);
        return response;
      } catch (ResourceAccessException e) {
        throw new PromptChainClientException(uriComponents.toUriString(), String.format("'%s' (PROBABLE CAUSE:"
                + " Either the host or port number of the specified baseUrl: '%s' is incorrect or the service"
                + " is not running)", getBaseUrl(), e.getMessage()), httpMethod, HttpStatus.BAD_REQUEST, e);
      } catch (RestClientException rce) {
        throw new PromptChainClientException(uriComponents.toUriString(),
                "An exception occurred while invoking the request: '" + rce.getMessage() + "'", httpMethod,
                HttpStatus.INTERNAL_SERVER_ERROR, rce);
      }
    } catch (PromptChainErrorResponseException | PromptChainClientException e) {
      throw e;
    } catch (RuntimeException re) {
      throw new PromptChainClientException("<todo>",
              "An exception occurred while invoking the request: '" + re.getMessage() + "'", httpMethod,
              HttpStatus.INTERNAL_SERVER_ERROR, re);
    }
  }

  public String getBaseUrl() {
    return baseUrl;
  }

  public final ObjectMapper getObjectMapper() {
    return objectMapper;
  }

  public RestClient getRestClient() {
    return restClient;
  }

}
