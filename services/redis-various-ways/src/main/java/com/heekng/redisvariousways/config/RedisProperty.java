package com.heekng.redisvariousways.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.data.redis")
public record RedisProperty(String host, Integer port, String password) {}
