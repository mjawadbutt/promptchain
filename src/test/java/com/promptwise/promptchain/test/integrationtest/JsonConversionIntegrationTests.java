package com.promptwise.promptchain.test.integrationtest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promptwise.promptchain.common.util.json.JacksonUtil;
import com.promptwise.promptchain.config.ApplicationProperties;
import com.promptwise.promptchain.model.MetricSignals;
import jakarta.validation.constraints.NotNull;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

@PromptChainIntegrationTestClass
class JsonConversionIntegrationTests {

  private static final String METRIC_SIGNALS_JSON = """
          {
            "resourceMetrics": [
              {
                "resource": {
                  "attributes": [
                    {
                      "key": "service.name",
                      "value": {
                        "stringValue": "payment-service"
                      }
                    }
                  ]
                },
                "scopeMetrics": [
                  {
                    "scope": {
                      "name": "com.example.metrics",
                      "version": "1.0.0"
                    },
                    "metrics": [
                      {
                        "name": "cpu.utilization",
                        "type": "gauge",
                        "unit": "percent",
                        "gauge": {
                          "dataPoints": [
                            {
                              "timeUnixNano": "1735322900123456789",
                              "attributes": [
                                {
                                  "key": "core",
                                  "value": {
                                    "stringValue": "0"
                                  }
                                }
                              ],
                              "asDouble": 0.65
                            }
                          ]
                        }
                      },
                      {
                        "name": "http.requests",
                        "type": "sum",
                        "unit": "count",
                        "sum": {
                          "aggregationTemporality": "CUMULATIVE",
                          "isMonotonic": true,
                          "dataPoints": [
                            {
                              "timeUnixNano": "1735322900123456789",
                              "attributes": [
                                {
                                  "key": "method",
                                  "value": {
                                    "stringValue": "GET"
                                  }
                                }
                              ],
                              "asInt": 1024
                            }
                          ]
                        }
                      },
                      {
                        "name": "http.request.duration",
                        "type": "histogram",
                        "unit": "ms",
                        "histogram": {
                          "aggregationTemporality": "DELTA",
                          "dataPoints": [
                            {
                              "timeUnixNano": "1735322900123456789",
                              "count": 5,
                              "sum": 123.0,
                              "bucketCounts": [
                                2,
                                2,
                                1
                              ],
                              "explicitBounds": [
                                50.0,
                                100.0
                              ],
                              "attributes": [
                                {
                                  "key": "endpoint",
                                  "value": {
                                    "stringValue": "/checkout"
                                  }
                                }
                              ]
                            }
                          ]
                        }
                      },
                      {
                        "name": "rpc.latency",
                        "type": "summary",
                        "unit": "ms",
                        "summary": {
                          "dataPoints": [
                            {
                              "timeUnixNano": "1735322900123456789",
                              "count": 10,
                              "sum": 250.5,
                              "quantileValues": [
                                {
                                  "quantile": 0.0,
                                  "value": 10.0
                                },
                                {
                                  "quantile": 0.5,
                                  "value": 25.0
                                },
                                {
                                  "quantile": 0.9,
                                  "value": 40.0
                                },
                                {
                                  "quantile": 0.99,
                                  "value": 45.0
                                }
                              ],
                              "attributes": [
                                {
                                  "key": "rpc.system",
                                  "value": {
                                    "stringValue": "grpc"
                                  }
                                }
                              ]
                            }
                          ]
                        }
                      }
                    ]
                  }
                ]
              }
            ]
          }""";
  private static final Logger LOGGER = LoggerFactory.getLogger(JsonConversionIntegrationTests.class);

  private final ApplicationProperties applicationProperties;
  private final ObjectMapper objectMapper;
  private final TestRestTemplate restTemplate;

  @Autowired
  public JsonConversionIntegrationTests(@NotNull final ApplicationProperties applicationProperties,
                                        @NotNull final ObjectMapper objectMapper,
                                        @NotNull final TestRestTemplate restTemplate) {
    this.applicationProperties = applicationProperties;
    this.objectMapper = objectMapper;
    this.restTemplate = restTemplate;
  }

  @BeforeAll
  void beforeRunningAnyTestCase() {
  }

  @Test
  @DisplayName("JSON CONVERSION TEST: MetricSignals, CASE: 'Serialize MetricSignals, deserialize, and compare to original.'")
  @Tags({@Tag("JsonConversion"), @Tag("IntegrationTests")})
  void testMetricSignalsJsonSerializationAndDeserialization() {
    String compactMetricSignalsJson = JacksonUtil.getInstance().compactJson(METRIC_SIGNALS_JSON);
    MetricSignals expecteMetricSignals = JacksonUtil.getInstance().deserializeJsonStringToObject(METRIC_SIGNALS_JSON,
            MetricSignals.class);
    String deserializedMetricSignalsJson = JacksonUtil.getInstance().serializeObjectToCompactJson(expecteMetricSignals);
    Assertions.assertThat(compactMetricSignalsJson).isEqualTo(deserializedMetricSignalsJson);
  }

  @AfterAll
  public void afterRunningAllTestCases() {
//    LOGGER.debug("Shutting down the application server.");
//    stopHsqlServer();
//    LOGGER.debug("Successfully shut down the application server.");
  }

  public ApplicationProperties getApplicationProperties() {
    return applicationProperties;
  }

  public ObjectMapper getObjectMapper() {
    return objectMapper;
  }

  public TestRestTemplate getRestTemplate() {
    return restTemplate;
  }

}
