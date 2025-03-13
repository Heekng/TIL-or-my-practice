package com.heekng.redisconcurrencylock.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.data.redis")
public record RedisProperty(String host, Short port, String password) {}
