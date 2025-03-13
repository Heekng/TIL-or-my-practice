package com.heekng.redisvariousways;

import com.redis.testcontainers.RedisContainer;
import org.junit.Ignore;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;

@Ignore
@SpringBootTest
@ContextConfiguration(initializers = IntegrationTest.IntegrationTestInitializer.class)
public class IntegrationTest {

    static RedisContainer redis;

    static {
        redis = new RedisContainer(RedisContainer.DEFAULT_IMAGE_NAME);
        redis.start();
    }

    static class IntegrationTestInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            HashMap<String, String> properties = new HashMap<>();

            String redisHost = redis.getHost();
            Integer redisPort = redis.getFirstMappedPort();

            properties.put("spring.data.redis.host", redisHost);
            properties.put("spring.data.redis.port", redisPort.toString());

            TestPropertyValues.of(properties).applyTo(applicationContext);
        }
    }
}
