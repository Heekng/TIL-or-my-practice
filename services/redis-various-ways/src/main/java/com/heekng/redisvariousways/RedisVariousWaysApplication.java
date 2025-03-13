package com.heekng.redisvariousways;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class RedisVariousWaysApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisVariousWaysApplication.class, args);
    }
}
