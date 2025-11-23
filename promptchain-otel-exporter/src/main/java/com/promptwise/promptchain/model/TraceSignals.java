package com.promptwise.promptchain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.promptwise.promptchain.common.util.json.JacksonUtil;

import java.util.List;

public class TraceSignals {

  private final List<TraceSignal> traceSignals;

  public TraceSignals(@JsonProperty("resourceSpans") final List<TraceSignal> traceSignals) {
    this.traceSignals = traceSignals == null ? List.of() : List.copyOf(traceSignals);
  }

  @JsonProperty("resourceSpans")
  public List<TraceSignal> getTraceSignals() {
    return traceSignals;
  }

  public static void main(String[] args) {
    String json = """
            {
              "resourceSpans": [
                {
                  "resource": {
                    "attributes": [
                      {
                        "key": "service.name",
                        "value": { "stringValue": "payment-service" }
                      }
                    ]
                  },
                  "scopeSpans": [
                    {
                      "scope": {
                        "name": "com.example.traces",
                        "version": "1.0.0"
                      },
                      "spans": [
                        {
                          "traceId": "4fd0b6133d2a4cbe9f3e4f4d0c0a4c1a",
                          "spanId": "89abcdef01234567",
                          "parentSpanId": "1234567890abcdef",
                          "name": "POST /checkout",
                          "kind": "SPAN_KIND_SERVER",
                          "startTimeUnixNano": "1735322900123456789",
                          "endTimeUnixNano": "1735322900456789012",
                          "attributes": [
                            {
                              "key": "http.method",
                              "value": { "stringValue": "POST" }
                            },
                            {
                              "key": "http.status_code",
                              "value": { "intValue": 200 }
                            }
                          ],
                          "status": {
                            "code": "STATUS_CODE_OK"
                          },
                          "events": [
                            {
                              "timeUnixNano": "1735322900234567890",
                              "name": "db.query",
                              "attributes": [
                                {
                                  "key": "db.statement",
                                  "value": { "stringValue": "SELECT * FROM orders WHERE id=123" }
                                }
                              ]
                            }
                          ]
                        }
                      ]
                    }
                  ]
                }
              ]
            }""";
    TraceSignals traceSignals1 = JacksonUtil.getInstance().deserializeJsonStringToObject(json, TraceSignals.class);
    System.out.println();
  }

  @Override
  public String toString() {
    return JacksonUtil.getInstance().serializeObjectToJsonString(this);
  }

}
