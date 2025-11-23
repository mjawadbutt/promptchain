package com.promptwise.promptchain.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class SumMetricDataPointDeserializer extends StdDeserializer<SumMetricDataPoint> {

  public SumMetricDataPointDeserializer() {
    super(SumMetricDataPoint.class);
  }

  @Override
  public SumMetricDataPoint deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    JsonNode node = p.getCodec().readTree(p);
    Class<? extends SumMetricDataPoint> targetClass;
    if (node.has("asDouble")) {
      targetClass = DoubleSumMetricDataPoint.class;
    } else if (node.has("asInt")) {
      targetClass = IntegerSumMetricDataPoint.class;
    } else {
      ObjectMapper mapper = (ObjectMapper) p.getCodec();
      String invalidJson = mapper.writeValueAsString(node);
      throw UnsupportedNumberMetricDataPointJsonException.create(invalidJson);
    }
    //-- Let Jackson deserialize the node into the target class
    return p.getCodec().treeToValue(node, targetClass);
  }
}
