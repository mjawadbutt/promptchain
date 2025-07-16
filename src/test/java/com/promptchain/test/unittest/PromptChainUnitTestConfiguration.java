package com.promptchain.test.unittest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hsqldb.server.Servlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

public class PromptChainUnitTestConfiguration {

  private final ApplicationProperties applicationProperties;
  private final ObjectMapper objectMapper;
  private final String dbFilePath;


  @Autowired
  public PromptChainUnitTestConfiguration(ApplicationProperties applicationProperties,
                                          ObjectMapper objectMapper,
                                          @Value("${applicationTestUnit.hsqldb.dbFilePath}") String dbFilePath) {
    this.applicationProperties = applicationProperties;
    this.objectMapper = objectMapper;
    this.dbFilePath = dbFilePath;
  }

  @Bean
  public PromptChainRestControllerClient promptChainRestControllerClient() {
    return new PromptChainRestControllerClient(getObjectMapper());
  }

//  @Bean
//  public PromptChainAdminRestControllerClient promptChainAdminRestControllerClient() {
//    return new PromptChainAdminRestControllerClient(getObjectMapper());
//  }

  /**
   * The name of the method should be the same as the one used when defining the bean the in the main configuration
   * because we are using bean overriding to define the mock rather than @MockBean because @MockBean dirties the
   * context and disallows it to be re-used between different test-method invocations. Allowing re-use runs the
   * tests faster. This should be the preferred approach for defining mocks even if we do have a situation in future
   * that would require context reloads.
   */
//  @Bean
//  public ClassOfControllerClientUsedByPromptChain classOfControllerClientUsedByPromptChain() {
//    //-- Note that Mockito never calls any constructor to create the mock object. It uses CGLib.
//    return Mockito.mock(ClassOfControllerClientUsedByPromptChain.class, MockReset.withSettings(MockReset.NONE));
//  }
//  @Bean
//  public RestClient createRestClient() {
//    return RestClient.create(getConfig());
//  }

  /**
   * This servlet enables outside connections to the DB if HSQL is being run in embedded mode.
   * Uncomment if using embedded mode and want other clients to be able to connect to the DB.
   * URL format: <code>jdbc:hsqldb:http://&lt;host>:&lt;port>/&lt;top-level-web-context>/database</code>
   * Example: <code>jdbc:hsqldb:http://localhost:8080/iflow-server/database</code>
   */
  @Bean
  public ServletRegistrationBean<Servlet> hsqldbServletBean() {
    ServletRegistrationBean<Servlet> bean = new ServletRegistrationBean<>(
            new Servlet(), WEB_CONTEXT + "/database");
    bean.setLoadOnStartup(1);
    bean.addInitParameter("hsqldb.server.database", getDbFilePath());
    return bean;
  }

  public ApplicationProperties getApplicationProperties() {
    return applicationProperties;
  }

  public ObjectMapper getObjectMapper() {
    return objectMapper;
  }

  public String getDbFilePath() {
    return dbFilePath;
  }

}
