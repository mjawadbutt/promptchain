package com.promptwise.promptchain.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.jcache.JCachingProvider;
import org.redisson.jcache.configuration.RedissonConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;
import java.util.Properties;

@Configuration
public class RedissonJCacheConfig {

  public static final String APP_CACHE = "AppCache";
  private static final Logger LOGGER = LoggerFactory.getLogger(RedissonJCacheConfig.class);

  private final ApplicationProperties applicationProperties;

  private final ObjectMapper objectMapper;

  public RedissonJCacheConfig(@NotNull final ApplicationProperties applicationProperties,
                              @NotNull final ObjectMapper objectMapper) {
    this.applicationProperties = applicationProperties;
    this.objectMapper = objectMapper;
  }

  /**
   * Defines the Redisson Config programmatically (instead via a yml file)
   */
  @Bean(destroyMethod = "shutdown") // Ensure RedissonClient is shut down when context closes
  public RedissonClient redissonClient() {
    LOGGER.info("Creating redisson config");
    Config config = new Config();
    config.useSingleServer()
            .setAddress(String.format("redis://%s:%d",
                    getApplicationProperties().getCachingProperties().getRedisHost(),
                    getApplicationProperties().getCachingProperties().getRedisPort()))
            .setPassword(null)
            .setDatabase(0)
            .setConnectionPoolSize(getApplicationProperties().getCachingProperties().getRedisConnectionPoolSize())
            .setConnectionMinimumIdleSize(getApplicationProperties().getCachingProperties().getRedisConnectionMinimumIdleSize())
            .setConnectTimeout(getApplicationProperties().getCachingProperties().getRedisTimeout());
    return Redisson.create(config);
  }

  /**
   * This is the method that provides the *Spring* CacheManager bean.
   * It wraps the underlying JSR-107 CacheManager.
   */
  @Bean
  public org.springframework.cache.CacheManager cacheManager(RedissonClient redissonClient) {
    LOGGER.info("Creating Spring's CacheManager");
    // Create the JSR-107 javax.cache.CacheManager (Redisson-backed)
    javax.cache.CacheManager jCacheManager = createJavaxCacheManager(redissonClient);
    createAndRegisterAppCache(jCacheManager, redissonClient);
    //-- Wrap the JSR-107 CacheManager in Spring's JCacheCacheManager so that we can use both type of caching
    //-- annotations (i.e., Spring and JSR 107)
    LOGGER.info("Finished creating Spring's CacheManager");
    return new JCacheCacheManager(jCacheManager);
  }

  /**
   * Creates and provides the raw JSR-107 javax.cache.CacheManager using Redisson's provider.
   * This is a helper method, not a direct Spring @Bean.
   */
  public javax.cache.CacheManager createJavaxCacheManager(RedissonClient redissonClient) {
    CachingProvider cachingProvider = Caching.getCachingProvider(JCachingProvider.class.getName());

    // As discussed, the URI and Properties here are often null
    // because the RedissonClient bean provides the primary connection config.
    javax.cache.CacheManager cacheManager = cachingProvider.getCacheManager(
            null, // URI: null, as we're not loading from a file for the CacheManager itself
            Thread.currentThread().getContextClassLoader(),
            new Properties() // Empty properties, unless Redisson's JCacheProvider has specific ones it understands here
    );
    return cacheManager;
  }

  private void createAndRegisterAppCache(javax.cache.CacheManager jCacheManager, RedissonClient redissonClient) {
    //-- Create a named jcache for general use.
    //-- Notes:
    //-- 1. The JSR-107 cache annotations do not require a named cache to be created (because they have
    //-- a default cache naming (fully qualified class name + method name + parameter types of the annotated
    //-- method eg: For: public String com.example.MyService.getData(String key), the default cache name
    //-- would be "com.example.MyService.getData(java.lang.String)").
    //--
    //-- 2. Spring's cache annotations DO require a name. Also having named caches is a best practice.
    //-- So we define a single named cache (for now) for general use. If we need more (like a separate cache for
    //-- entities with specialized config like ram, persistent storage etc., we cna do that later, however note
    //-- that for persistence, the redis server needs also to be configured for persistent storage which it
    //-- currently is not, see the compose file for redis)
    //-
    //-- 3. Since we are using Redisson, we can also do Redisson specific cache config if needed (i.e. instead of
    //-- using MutableConfiguration, use Redisson specific config classes and cast the manager to Redisson
    //-- specific impl and create it.
    MutableConfiguration<String, String> appCacheConfig = new MutableConfiguration<>();
    appCacheConfig.setStoreByValue(true)
            .setManagementEnabled(true)
            .setStatisticsEnabled(true);
    //-- You could add ExpiryPolicy, CacheEntryListener, etc. here

    //-- Link this specific cache to the shared RedissonClient bean
    javax.cache.configuration.Configuration<String, String> redissonJCacheConfig =
            RedissonConfiguration.fromInstance(redissonClient, appCacheConfig);

    jCacheManager.createCache(APP_CACHE, redissonJCacheConfig);
  }

  public ApplicationProperties getApplicationProperties() {
    return applicationProperties;
  }

  public ObjectMapper getObjectMapper() {
    return objectMapper;
  }

}