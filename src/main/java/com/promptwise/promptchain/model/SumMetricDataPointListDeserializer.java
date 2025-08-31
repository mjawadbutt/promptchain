package com.promptwise.promptchain.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SumMetricDataPointListDeserializer extends JsonDeserializer<List<SumMetricDataPoint>> {

  private final SumMetricDataPointDeserializer sumMetricDataPointDeserializer = new SumMetricDataPointDeserializer();

  @Override
  public List<SumMetricDataPoint> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    JsonNode node = p.getCodec().readTree(p);
    List<SumMetricDataPoint> result = new ArrayList<>();
    for (JsonNode element : node) {
      JsonParser elementParser = element.traverse(p.getCodec());
      elementParser.nextToken();
      SumMetricDataPoint obj = sumMetricDataPointDeserializer.deserialize(elementParser, ctxt);
      result.add(obj);
    }
    return result;
  }

}
