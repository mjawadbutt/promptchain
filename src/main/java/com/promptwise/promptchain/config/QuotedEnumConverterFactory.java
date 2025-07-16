package com.promptwise.promptchain.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

public class QuotedEnumConverterFactory implements ConverterFactory<String, Enum> {

  private static final Logger LOGGER = LoggerFactory.getLogger(QuotedEnumConverterFactory.class);

  @Override
  public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
    return new StringToEnumConverter<>(targetType);
  }

  private static class StringToEnumConverter<T extends Enum> implements Converter<String, T> {

    private final Class<T> enumType;

    public StringToEnumConverter(Class<T> enumType) {
      this.enumType = enumType;
    }

    @Override
    public T convert(String source) {
      if (source == null || source.trim().isEmpty()) {
        return null;
      }

      // Remove quotes from the string
      String unquotedSource = source.trim().replaceAll("^\"|\"$", "");

      try {
        // Use Enum.valueOf with the specific enumType
        return (T) Enum.valueOf(this.enumType, unquotedSource.toUpperCase());
      } catch (IllegalArgumentException e) {
        // Handle cases where the unquoted string does not match an enum constant
        //TODO-Exception: Thrown proper exception
        LOGGER.error("Cannot convert '" + source + "' to " + enumType.getSimpleName() + " enum. Unquoted: '" + unquotedSource + "'");
        return null; // Or throw new ConversionFailedException(...)
      }
    }
  }
}