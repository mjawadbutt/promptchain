package com.promptchain.tests.unittests;

import org.springframework.beans.factory.annotation.Autowired;

public class PromptChainUnitTestConfiguration {


  @Autowired
  public PromptChainUnitTestConfiguration() {
  }

  /**
   * The name of the method should be the same as the one used when defining the bean the in the main configuration
   * because we are using bean overriding to define the mock rather than @MockBean because @MockBean dirties the
   * context and disallows it to be re-used between different test-method invocations. Allowing re-use runs the
   * tests faster. This should be the preferred approach for defining mocks even if we do have a situation in future
   * that would require context reloads.
   */
//  @Bean
//  public DocumentStoreAndRetrievalService documentStoreAndRetrievalService() {
//    return new MockRetrievalServiceImpl();
//  }
//
//  @Bean
//  public VotsDocumentClientService votsDocumentClientService() {
//    return new MockVotsDocumentClientService();
//  }

}
