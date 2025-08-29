package com.promptwise.promptchain.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class NumberMetricDataPointDeserializer extends StdDeserializer<NumberMetricDataPoint> {

  public NumberMetricDataPointDeserializer() {
    super(NumberMetricDataPoint.class);
  }

  @Override
  public NumberMetricDataPoint deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    JsonNode node = p.getCodec().readTree(p);
    Class<? extends NumberMetricDataPoint> targetClass;
    if (node.has("asDouble")) {
      targetClass = DoubleGaugeMetricDataPoint.class;
    } else if (node.has("asInt")) {
      targetClass = IntegerGaugeMetricDataPoint.class;
    } else {
      ObjectMapper mapper = (ObjectMapper) p.getCodec();
      String invalidJson = mapper.writeValueAsString(node);
      throw UnsupportedNumberMetricDataPointJsonException.create(invalidJson);
    }
    //-- Let Jackson deserialize the node into the target class
    return p.getCodec().treeToValue(node, targetClass);
  }
}
