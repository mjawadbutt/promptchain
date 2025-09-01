package com.promptwise.promptchain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.promptwise.promptchain.common.util.json.JacksonUtil;

import java.util.List;

public class LogSignals {

  private final List<LogSignal> logSignals;

  public LogSignals(@JsonProperty("resourceLogs") final List<LogSignal> logSignals) {
    this.logSignals = logSignals == null ? List.of() : List.copyOf(logSignals);
  }

  @JsonProperty("resourceLogs")
  public List<LogSignal> getLogSignals() {
    return logSignals;
  }

  public static void main(String[] args) {
    String json = """
            {
              "resourceLogs": [
                {
                  "resource": {
                    "attributes": [
                      {
                        "key": "service.name",
                        "value": { "stringValue": "payment-service" }
                      }
                    ]
                  },
                  "scopeLogs": [
                    {
                      "scope": {
                        "name": "com.example.logs",
                        "version": "1.0.0"
                      },
                      "logRecords": [
                        {
                          "timeUnixNano": "1735322900123456789",
                          "severityNumber": 17,
                          "severityText": "ERROR",
                          "body": { "stringValue": "Payment processing failed" },
                          "attributes": [
                            {
                              "key": "transaction.id",
                              "value": { "stringValue": "txn-12345" }
                            },
                            {
                              "key": "error.type",
                              "value": { "stringValue": "TimeoutException" }
                            }
                          ],
                          "traceId": "4fd0b6133d2a4cbe9f3e4f4d0c0a4c1a",
                          "spanId": "89abcdef01234567"
                        }
                      ]
                    }
                  ]
                }
              ]
            }""";
    LogSignals logSignals1 = JacksonUtil.getInstance().deserializeJsonStringToObject(json, LogSignals.class);
    System.out.println();
  }

  @Override
  public String toString() {
    return JacksonUtil.getInstance().serializeObjectToJsonString(this);
  }

}
