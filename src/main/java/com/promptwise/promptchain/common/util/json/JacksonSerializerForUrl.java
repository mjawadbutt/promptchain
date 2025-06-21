package com.promptwise.promptchain.common.util.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.promptwise.promptchain.common.util.ResourceLoaderUtil;

import java.io.IOException;
import java.net.URL;

public class JacksonSerializerForUrl extends JsonSerializer<URL> {
  @Override
  public void serialize(final URL value, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
    gen.writeString(ResourceLoaderUtil.serializeUrl(value));
  }

}
