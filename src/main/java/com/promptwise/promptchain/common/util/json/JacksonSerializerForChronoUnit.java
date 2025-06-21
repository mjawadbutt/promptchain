package com.promptwise.promptchain.common.util.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.common.base.CaseFormat;

import java.io.IOException;
import java.time.temporal.ChronoUnit;

public class JacksonSerializerForChronoUnit extends StdSerializer<ChronoUnit> {
  public JacksonSerializerForChronoUnit() {
    super(ChronoUnit.class);
  }

  public JacksonSerializerForChronoUnit(Class<ChronoUnit> t) {
    super(t);
  }

  public void serialize(ChronoUnit chronoUnit, JsonGenerator generator, SerializerProvider provider)
          throws IOException {
    generator.writeFieldName(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, chronoUnit.name()));
  }

}
