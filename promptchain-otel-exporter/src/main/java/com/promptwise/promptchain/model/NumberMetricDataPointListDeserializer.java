package com.promptwise.promptchain.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NumberMetricDataPointListDeserializer extends JsonDeserializer<List<NumberMetricDataPoint>> {

  private final NumberMetricDataPointDeserializer numberMetricDataPointDeserializer = new NumberMetricDataPointDeserializer();

  @Override
  public List<NumberMetricDataPoint> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    JsonNode node = p.getCodec().readTree(p);
    List<NumberMetricDataPoint> result = new ArrayList<>();
    for (JsonNode element : node) {
      JsonParser elementParser = element.traverse(p.getCodec());
      elementParser.nextToken();
      NumberMetricDataPoint obj = numberMetricDataPointDeserializer.deserialize(elementParser, ctxt);
      result.add(obj);
    }
    return result;
  }

}
