package com.heekng.howtouses3;

import org.junit.Ignore;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Ignore
@SpringBootTest
@ContextConfiguration(initializers = IntegrationTest.IntegrationTestInitializer.class)
public class IntegrationTest {

    static LocalStackContainer aws;

    static {
        DockerImageName imageName = DockerImageName.parse("localstack/localstack:latest");
        aws = new LocalStackContainer(imageName)
                .withServices(LocalStackContainer.Service.S3)
                .withStartupTimeout(Duration.ofMinutes(10));
        aws.start();
    }

    static class IntegrationTestInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            Map<String, String> properties = new HashMap<>();

            try {
                aws.execInContainer(
                        "awslocal",
                        "s3api",
                        "create-bucket",
                        "--bucket",
                        "file-bucket"
                );
                properties.put("aws.endpoint", aws.getEndpoint().toString());
            } catch (Exception e) {}

            TestPropertyValues.of(properties)
                    .applyTo(applicationContext);
        }
    }
}
