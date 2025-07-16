package com.promptchain.test.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promptwise.promptchain.common.util.Rfc7807CompliantHttpRequestProcessingErrorResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;

import static com.promptwise.promptchain.PromptChainApplication.*;

public class PromptChainAdminRestControllerClient extends AbstractIPromptChainControllerClient {

  public PromptChainAdminRestControllerClient(@NotNull final String baseUrl, @NotNull final ObjectMapper objectMapper) {
    super(baseUrl, objectMapper);
  }

  public ClientEntity createClient(@NotNull final CreateClientRequest createClientRequest) {
    Assert.notNull(createClientRequest, "The parameter 'createClientRequest' cannot be 'null'");

    ClientEntity clientEntity = invokeRequest(HttpMethod.POST,
            WEB_CONTEXT + "/createClient", MediaType.APPLICATION_JSON, null,
            null, MediaType.APPLICATION_JSON,
            (bodySpec) -> {
              return bodySpec
                      .body(createClientRequest)
                      .retrieve()
                      .onStatus(httpStatusCode -> httpStatusCode != HttpStatus.OK, (request, response) -> {
                        Rfc7807CompliantHttpRequestProcessingErrorResponse errorResponse = getObjectMapper().readValue(
                                response.getBody(), Rfc7807CompliantHttpRequestProcessingErrorResponse.class);
                        throw new IFlowXwsErrorResponseException(errorResponse);
                      }).body(ClientEntity.class);
            });
    return clientEntity;
  }

}
