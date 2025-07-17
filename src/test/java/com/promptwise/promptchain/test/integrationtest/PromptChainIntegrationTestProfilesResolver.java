package com.promptwise.promptchain.test.integrationtest;

import org.springframework.test.context.ActiveProfilesResolver;

public class PromptChainIntegrationTestProfilesResolver implements ActiveProfilesResolver {

  @Override
  public String[] resolve(Class<?> testClass) {
    boolean isWindows = System.getProperty("os.name").toLowerCase().contains("windows");
    //-- Note that the order of returned profiles matters, for example if a profile "p1" is before "p2" in
    //-- the returned array then properties defined in the p2 property file (i.e. application-p2.properties) will
    //-- override properties defined in the p1 property file (i.e. application-p1.properties)
    //-- Also note that since we are @Importing both main application config and the unit test config (see
    //-- @Import in LrvViewUnitTestClass) so profiles and property files of both the main application and the tests
    //-- would be visible to the test context and can be applied in any order to achieve the desired overriding
    //-- behaviour.
    if (isWindows) {
      //-- The profiles i.e. property file load order for running JUnit tests on Windows locally.
      //-- testUnitWinLocal overrides test, test overrides winLocal. Then all props are merged and available for use
      // anywhere and in any way (@Value, @ConfigurationProperties, Environment bean etc.)
      return new String[]{"winLocal", "test", "testIntegration", "testIntegrationWinLocal"};
    } else {
      //-- The profiles i.e. property file load order for running JUnit tests on Linux locally (will be used by
      //-- Jenkins pipeline builds)
      return new String[]{"linuxLocal", "test", "testIntegration", "testIntegrationLinuxLocal"};
    }
  }
}