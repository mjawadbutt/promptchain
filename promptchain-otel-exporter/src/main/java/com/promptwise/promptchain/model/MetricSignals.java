package com.promptwise.promptchain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.promptwise.promptchain.common.util.json.JacksonUtil;

import java.util.List;

public class MetricSignals {

  private final List<MetricSignal> metricSignals;

  public MetricSignals(@JsonProperty("resourceMetrics") final List<MetricSignal> metricSignals) {
    this.metricSignals = metricSignals == null ? List.of() : List.copyOf(metricSignals);
  }

  @JsonProperty("resourceMetrics")
  public List<MetricSignal> getMetricSignals() {
    return metricSignals;
  }

  public static void main(String[] args) {

    String json = """
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
                          "type": "GAUGE",
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
                          "type": "SUM",
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
                          "type": "HISTOGRAM",
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
                                  50,
                                  100
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
                          "type": "SUMMARY",
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
    MetricSignals metricSignals1 = JacksonUtil.getInstance().deserializeJsonStringToObject(json, MetricSignals.class);
    System.out.println();
  }

  @Override
  public String toString() {
    return JacksonUtil.getInstance().serializeObjectToJsonString(this);
  }

}
