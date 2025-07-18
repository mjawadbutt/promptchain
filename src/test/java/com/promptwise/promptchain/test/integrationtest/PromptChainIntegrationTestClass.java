package com.promptwise.promptchain.test.integrationtest;

import com.promptwise.promptchain.PromptChainApplication;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//-- https://stackoverflow.com/questions/39690094/spring-boot-default-profile-for-integration-tests
//-- It is better to user @ActiveProfiles rather than @TestPropertySource because then we can use Spring's
//-- default property file naming conventions, and, also utilize autoconfiguration).
//-- @TestPropertySource should be for loading additional property files with non-default file names (i.e. other
//-- than application.properties or application-<profile>.properties).
//-- See example below:
//-- @TestPropertySource(locations = {"classpath:config/additional1.properties", "classpath:config/additional2.properties", "file:additional3.properties"})

//-- Note that in @ActiveProfiles, the order of profiles matters, for example if a profile "p1" is defined before
//-- "p2" then properties defined in the profile specific property file of p2 (i.e. application-p2.properties) can
//-- override properties defined in the profile specific property file of p1 (i.e. application-p1.properties)
//@ActiveProfiles({"test", "unittestwindows", "winLocal"})

//-- The above has been commented out (but left for reference) because we need to determine the OS on which the tests
//-- are running to be able to set the order of profiles (which essentially means the order of property file loading).
//-- And it seems that the OS cannot be determined statically and needs to be done programmatically by implementing
//-- an ActiveProfilesResolver.
@ActiveProfiles(resolver = PromptChainIntegrationTestProfilesResolver.class)

//-- It is very important that the test-configuration is AFTER the application-configuration so that the
//-- test-configuration can properly utilize the bean overriding feature to override any bean(s) (as needed) in the
//-- application-configuration
@Import({PromptChainIntegrationTestConfiguration.class})
public @interface PromptChainIntegrationTestClass {
}
