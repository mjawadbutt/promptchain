package com.promptwise.promptchain.common.util.json;


import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.promptwise.promptchain.common.util.CommonUtilSystemException;
import com.promptwise.promptchain.common.util.ResourceLoaderUtil;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

public class JacksonUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(JacksonUtil.class);
  private static JacksonUtil jacksonUtil = null;

  private final ObjectMapper objectMapper;

  public JacksonUtil(@NotNull final ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public <T> T deserializeJsonStringToObject(String str, Class<T> clazz) {
    try {
      return getObjectMapper().readValue(str, clazz);
    } catch (JacksonException je) {
      throw CommonUtilSystemException.create(String.format("""
              Unable to deserialize the string '%s' object into the type '%s'. Please see root cause for further info.
              """, str, clazz.getName()), je);
    }
  }

  public <T> T deserializeJsonStringToObject(String str, TypeReference<T> typeReference) {
    try {
      return getObjectMapper().readValue(str, typeReference);
    } catch (JacksonException je) {
      throw CommonUtilSystemException.create(String.format("""
              Unable to deserialize the string '%s' object into the type '%s'. Please see root cause for further info.
              """, str, typeReference.getType().getTypeName()), je);
    }
  }

  public <T> T deserializeJsonStringToObject(String str, JavaType javaType) {
    try {
      return getObjectMapper().readValue(str, javaType);
    } catch (JacksonException je) {
      throw CommonUtilSystemException.create(String.format("""
              Unable to deserialize the string '%s' object into the type '%s'. Please see root cause for further info.
              """, str, javaType.getTypeName()), je);
    }
  }

  public String serializeObjectToJsonString(Object o) {
    try {
      return getObjectMapper().writeValueAsString(o);
    } catch (JacksonException je) {
      throw CommonUtilSystemException.create(String.format("""
              Unable to serialize the given object '%s' to JSON string! Please see root cause for further info.
              """, o), je);
    }
  }

  public static synchronized JacksonUtil getInstance() {
    if (jacksonUtil == null) {
      Map<String, String> buildInfoPropertyMap;
      try {
        buildInfoPropertyMap = ResourceLoaderUtil.loadPropertiesFromResourceUrlString(
                "classpath:META-INF/build-info.properties");
      } catch (IOException ioe) {
        LOGGER.warn("Unable to load module info for ObjectMapper because the file '"
                + "classpath:META-INF/build-info.properties' does not exist!)");
        buildInfoPropertyMap = Collections.emptyMap();
      }

      //-- Create a 'standard' ObjectMapper that can be used throughout the application. This is being done here
      //-- so that the ObjectMapper is available for static contexts / Class level as well and not just at Instance level.
      String objectMapperModuleName = buildInfoPropertyMap.get("build.name");
      String groupId = buildInfoPropertyMap.get("build.group");
      String artifactId = buildInfoPropertyMap.get("build.artifact");
      Integer majorVersion = Integer.valueOf(buildInfoPropertyMap.get("build.majorVersion"));
      Integer minorVersion = Integer.valueOf(buildInfoPropertyMap.get("build.minorVersion"));
      Integer incrementalVersion = Integer.valueOf(buildInfoPropertyMap.get("build.incrementalVersion"));
      String snapshotQualifier = buildInfoPropertyMap.get("build.qualifier");

      ObjectMapper baseObjectMapper = createBaseObjectMapper(objectMapperModuleName, majorVersion, minorVersion,
              incrementalVersion, snapshotQualifier, groupId, artifactId);
      //-- Use the same Class and Constructor that SpringBoot uses so that the default configuration of the
      //-- ObjectMapper is consistent with SpringBoot.
      Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
      builder.featuresToEnable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
      builder.configure(baseObjectMapper);
      ObjectMapper standardObjectMapper = builder.build();

      //-- Wrap the standard ObjectMapper into a utility class that will be used throughout the application and makes it
      //-- easier to use the 'standard' ObjectMapper created above.
      jacksonUtil = new JacksonUtil(standardObjectMapper);
    }
    return jacksonUtil;
  }

  private static ObjectMapper createBaseObjectMapper(final String objectMapperModuleName, final Integer majorVersion,
                                                     final Integer minorVersion, final Integer incrementalVersion,
                                                     final String snapshotQualifier, final String groupId,
                                                     final String artifactId) {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    objectMapper.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
    objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
//    objectMapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    objectMapper.setDateFormat((new StdDateFormat()).withColonInTimeZone(true));
    Module module = new JavaTimeModule();
    objectMapper.registerModule(module);
    objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
    Version version = new Version(majorVersion, minorVersion, incrementalVersion, snapshotQualifier, groupId, artifactId);

    SimpleModule simpleModule = new SimpleModule((
            objectMapperModuleName == null ? "" : objectMapperModuleName + "-") + "ObjectMapper-SimpleModule", version);
    simpleModule.addSerializer(URL.class, new JacksonSerializerForUrl());
    simpleModule.addDeserializer(URL.class, new JacksonDeserializerForUrl());
    objectMapper.registerModule(simpleModule);
    //-- Disable context based time zone and date time adjustment in case date string contains timezone info.
    //-- This feature is only applicable to Java8 date-time types (ZonedDateTime, OffsetDateTime, LocalDateTime)
    //-- Background:
    //-- When this feature is enabled (default), then Jackson does the following while deserializing a date-time string:
    //-- 1. Whether a timezone offset is specified in the String being deserialized or not, the timezone of the
    //      result is set to the timezone set in the ObjectMapper's context timezone (configured via
    //      objectMapper.setTimeZone(TimeZone))
    //   2. The date & time of the result is adjusted as per the ObjectMapper's context timezone.
    //-- When disabled, then Jackson does the following while deserializing:
    //-- 1. If the String has timezone info (i.e. a UTC offset such as "+11:00", and, optionally, a timezone ID
    //--    such as "Australia/Sydney"), then it is used AS IS and no date time adjustment occurs.
    //-- 2. If the String does not have timezone info then the behaviour is as if this feature is enabled (i.e.
    //--    timezone is set to the ObjectMapper's context timezone and date-time value is adjusted to that timezone).
    objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
    return objectMapper;
  }

  public final ObjectMapper getObjectMapper() {
    return objectMapper;
  }

}
