package com.promptwise.promptchain.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class RedissonConfig {

  @Value("${spring.redis.config-path:redisson.yaml}")
  private String configPath;

  @Bean(destroyMethod = "shutdown")
  public RedissonClient redissonClient() throws IOException {
    Config config = Config.fromYAML(new ClassPathResource(configPath).getInputStream());
    RedissonClient redisson = Redisson.create(config);

    // Wait for Redis to become available
    int retries = 10;
    while (retries-- > 0) {
      try {
        redisson.getKeys().count(); // ping Redis
        return redisson;
      } catch (Exception e) {
        System.out.println("Waiting for Redis... retries left: " + retries);
        try {
          Thread.sleep(1000); // 1 sec delay between retries
        } catch (InterruptedException ignored) {
        }
      }
    }
    throw new IllegalStateException("Unable to connect to Redis after retries.");
  }

}