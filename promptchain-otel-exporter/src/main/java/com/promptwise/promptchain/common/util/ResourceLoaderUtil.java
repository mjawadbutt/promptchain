package com.promptwise.promptchain.common.util;

import jakarta.validation.constraints.NotNull;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class ResourceLoaderUtil {

  private ResourceLoaderUtil() {
  }

  public static Map<String, String> loadPropertiesFromResourceUrl(@NotNull final URL propertiesResourceUrl,
                                                                  final ClassLoader classLoader)
          throws IOException {
    Map<String, String> propertiesMap = loadPropertiesFromResourceUrlString(serializeUrl(propertiesResourceUrl),
            classLoader);
    return propertiesMap;
  }

  public static Map<String, String> loadPropertiesFromResourceUrlString(
          @NotNull final String propertiesResourceUrlString, final ClassLoader classLoader)
          throws IOException {
    String propertiesResourceContents = getContentFromResourceUrlStringAsString(
            propertiesResourceUrlString, classLoader);
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

  public static String getContentFromResourceUrlAsString(@NotNull final URL resourceUrl,
                                                         final ClassLoader classLoader) throws IOException {
    return getContentFromResourceUrlStringAsString(serializeUrl(resourceUrl), classLoader);
  }

  public static String getContentFromResourceUrlStringAsString(@NotNull final String resourceUrlString,
                                                               final ClassLoader classLoader)
          throws IOException {
    Resource resource = getResourceLoader(classLoader).getResource(resourceUrlString);
    return resource.getContentAsString(StandardCharsets.UTF_8);
  }

  public static byte[] getContentFromResourceUrlStringAsByteArray(@NotNull final String resourceUrlString,
                                                                  final ClassLoader classLoader)
          throws IOException {
    Resource resource = getResourceLoader(classLoader).getResource(resourceUrlString);
    return resource.getContentAsByteArray();
  }

  public static URL deserializeUrl(@NotNull final String string, final ClassLoader classLoader) throws IOException {
    Resource resource = getResourceLoader(classLoader).getResource(string);
    URL url = resource.getURL();
    return url;
  }

  public static String serializeUrl(@NotNull final URL url) {
    return url.toExternalForm();
  }

  public static ResourceLoader getResourceLoader(ClassLoader classLoader) {
    return new DefaultResourceLoader(classLoader);
  }

}
