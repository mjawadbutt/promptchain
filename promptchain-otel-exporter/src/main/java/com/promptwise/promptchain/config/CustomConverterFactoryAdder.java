package com.promptwise.promptchain.config;

import org.springframework.format.FormatterRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @see https://github.com/spring-projects/spring-boot/issues/29586
 */
@Component
public class CustomConverterFactoryAdder implements WebMvcConfigurer {

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverterFactory(new QuotedEnumConverterFactory());
  }

}