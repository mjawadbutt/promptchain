package com.promptwise.promptchain.common.util;

import jakarta.validation.constraints.NotNull;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class ResourceLoaderUtil {

  private static final DefaultResourceLoader RESOURCE_LOADER = new DefaultResourceLoader();

  private ResourceLoaderUtil() {
  }

  public static Map<String, String> loadPropertiesFromResourceUrl(@NotNull final URL propertiesResourceUrl)
          throws IOException {
    Map<String, String> propertiesMap = loadPropertiesFromResourceUrlString(serializeUrl(propertiesResourceUrl));
    return propertiesMap;
  }

  public static Map<String, String> loadPropertiesFromResourceUrlString(
          @NotNull final String propertiesResourceUrlString)
          throws IOException {
    String propertiesResourceContents = getContentFromResourceUrlStringAsString(propertiesResourceUrlString);
    Map<String, String> propertiesMap = loadPropertiesFromPropertiesString(propertiesResourceContents);
    return propertiesMap;
  }

  public static Map<String, String> loadPropertiesFromPropertiesString(@NotNull final String propertiesString)
          throws IOException {
    StringReader sr = new StringReader(propertiesString);
    Properties properties = new Properties();
    properties.load(sr);
    Map<String, String> propertiesMap = new HashMap<>();
    propertiesMap.putAll(properties.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().toString(),
            e -> e.getValue().toString())));
    return propertiesMap;
  }

  public static String getContentFromResourceUrlAsString(@NotNull final URL resourceUrl) throws IOException {
    return getContentFromResourceUrlStringAsString(serializeUrl(resourceUrl));
  }

  public static String getContentFromResourceUrlStringAsString(@NotNull final String resourceUrlString)
          throws IOException {
    Resource resource = RESOURCE_LOADER.getResource(resourceUrlString);
    return resource.getContentAsString(StandardCharsets.UTF_8);
  }

  public static byte[] getContentFromResourceUrlStringAsByteArray(@NotNull final String resourceUrlString)
          throws IOException {
    Resource resource = RESOURCE_LOADER.getResource(resourceUrlString);
    return resource.getContentAsByteArray();
  }

  public static URL deserializeUrl(@NotNull final String string) throws IOException {
    Resource resource = RESOURCE_LOADER.getResource(string);
    URL url = resource.getURL();
    return url;
  }

  public static String serializeUrl(@NotNull final URL url) {
    return url.toExternalForm();
  }

}
