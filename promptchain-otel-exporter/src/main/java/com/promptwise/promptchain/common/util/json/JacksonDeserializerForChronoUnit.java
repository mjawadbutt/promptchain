package com.promptwise.promptchain.common.util.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.google.common.base.CaseFormat;

import java.io.IOException;
import java.time.temporal.ChronoUnit;

public class JacksonDeserializerForChronoUnit extends StdDeserializer<ChronoUnit> {

  public JacksonDeserializerForChronoUnit(Class<?> vc) {
    super(vc);
  }

  public JacksonDeserializerForChronoUnit(JavaType valueType) {
    super(valueType);
  }

  public JacksonDeserializerForChronoUnit(StdDeserializer<?> src) {
    super(src);
  }

  @Override
  public ChronoUnit deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
    JsonNode node = jsonParser.getCodec().readTree(jsonParser);
    String lowerCamelCaseEnumName = node.asText();
    String enumName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, lowerCamelCaseEnumName);
    ChronoUnit chronoUnit = ChronoUnit.valueOf(enumName);
    return chronoUnit;
  }

//  @Override
//  public ChronoUnit deserialize(JsonParser jsonParser, DeserializationContext ctxt)
//          throws IOException {
//  }

}
