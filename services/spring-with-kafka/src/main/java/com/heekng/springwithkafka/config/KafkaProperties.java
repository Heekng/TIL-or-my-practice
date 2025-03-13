package com.heekng.springwithkafka.config;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.kafka")
public record KafkaProperties(Producer producer, Consumer consumer) {
    public record Producer(
        List<String> bootstrapServers,
        String clientId,
        String acks,
        boolean enableIdempotence,
        int minInSyncReplicas,
        int maxInFlightRequestsPerConnection
    ) {}

    public record Consumer(
        List<String> bootstrapServers,
        String clientId,
        String groupId,
        String timeoutMs,
        int fetchMinBytes,
        int fetchMaxWaitMs,
        String autoOffsetReset,
        int maxPollRecords,
        boolean enableAutoCommit
    ){}
}
