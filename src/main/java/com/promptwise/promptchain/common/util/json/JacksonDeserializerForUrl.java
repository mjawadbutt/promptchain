package com.promptwise.promptchain.common.util.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.promptwise.promptchain.common.util.ResourceLoaderUtil;

import java.io.IOException;
import java.net.URL;

public class JacksonDeserializerForUrl extends JsonDeserializer<URL> {

  @Override
  public URL deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext)
          throws IOException {
    String urlString = jsonParser.getText();
    URL url = ResourceLoaderUtil.deserializeUrl(urlString);
    return url;
  }

}
