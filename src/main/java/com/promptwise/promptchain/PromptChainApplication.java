package com.promptwise.promptchain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promptwise.promptchain.common.util.ApplicationBuildInfo;
import com.promptwise.promptchain.common.util.ApplicationRuntimeInfo;
import com.promptwise.promptchain.common.util.fileio.FileIoUtil;
import com.promptwise.promptchain.common.util.json.JacksonUtil;
import com.promptwise.promptchain.config.ApplicationProperties;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;

import java.nio.file.Path;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationProperties.class})
@EnableCaching
@EnableRetry
public class PromptChainApplication {

  public static final String WEB_CONTEXT = "/promptchain";
  private static final Logger LOGGER = LoggerFactory.getLogger(PromptChainApplication.class);

  //-- Even though there is no real need for storing these three in this class other than for ease of debugging,
  //-- however, let's not remove them since it is not causing any issues.
  private final ApplicationProperties applicationProperties;
  private final ApplicationRuntimeInfo applicationRuntimeInfo;
  private final ApplicationBuildInfo applicationBuildInfo;
  private final ApplicationContext applicationContext;

  public PromptChainApplication(ApplicationProperties applicationProperties,
                                ApplicationRuntimeInfo applicationRuntimeInfo,
                                ApplicationBuildInfo applicationBuildInfo,
                                ApplicationContext applicationContext) {
    this.applicationProperties = applicationProperties;
    this.applicationRuntimeInfo = applicationRuntimeInfo;
    this.applicationBuildInfo = applicationBuildInfo;
    this.applicationContext = applicationContext;
  }

  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(PromptChainApplication.class, args);
  }

  @PostConstruct
  public void postConstruct() throws InvalidApplicationConfigurationException {
    LOGGER.info("APPLICATION-CONTEXT INITIALIZATION COMPLETED SUCCESSFULLY!");
    LOGGER.info("APPLICATION BUILD-INFO IS AS FOLLOWS:");
    LOGGER.info("{}", getApplicationBuildInfo());
    LOGGER.info("APPLICATION RUNTIME-INFO IS AS FOLLOWS:");
    LOGGER.info("{}", getApplicationRuntimeInfo());
    LOGGER.info("APPLICATION PROPERTIES ARE AS FOLLOWS:");
    LOGGER.info("{}", getApplicationProperties());
    Path tempDir = getApplicationProperties().getTempDir();
    //TODO-Jawad: Similar application-property validations can be added in future here.
    //-- Make sure rootDir exists on the file-system, is a directory, and DocMaint has read/write access to it.
    try {
      FileIoUtil.createDirectories(tempDir);
    } catch (Exception e) {
      throw new InvalidApplicationConfigurationException(String.format("""
              The value: %s of the config-property 'tempDir' is not valid as it does not meet one or more
              of the following criteria:
              1. The path did not exist and DocMaint was unable to create it.
              2. The path already existed but was a file rather than a directory.
              3. The path exists and is a directory, but Docmaint does not have write access to it""", tempDir), e);
    }
  }

  @PreDestroy
  public void preDestroy() {
    LOGGER.info("Shutting down the application server.");
//    stopHsqlServer();
    LOGGER.info("Successfully shut down the application server.");
  }

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = getJacksonUtil().getObjectMapper();
    return objectMapper;
  }

//  /**
//   * The ExitCodeGenerator interface may be implemented by exceptions. When such an exception is encountered,
//   * Spring Boot returns the exit code provided by the implemented getExitCode() method.
//   *
//   * @return The exit code. non-zero value will indicate abnormal exit to the OS.
//   */
//    @Bean
//    public ExitCodeGenerator exitCodeGenerator() {
//        return () -> 0;
//    }

  public <T extends Object> T getBean(Class<T> beanClass) {
    return getApplicationContext().getBean(beanClass);
  }

  public ApplicationProperties getApplicationProperties() {
    return applicationProperties;
  }

  public ApplicationRuntimeInfo getApplicationRuntimeInfo() {
    return applicationRuntimeInfo;
  }

  public ApplicationBuildInfo getApplicationBuildInfo() {
    return applicationBuildInfo;
  }

  public ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  public static JacksonUtil getJacksonUtil() {
    return JacksonUtil.getInstance();
  }

}
