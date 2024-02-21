package com.heekng.springwithkafka.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.requests.CreateTopicsRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
@EnableKafka
public class TopicConfig {

    public static final String TOPIC_NAME_1 = "test-topic-1";
    public static final String TOPIC_NAME_2 = "test-topic-2";
    public static final String TOPIC_NAME_3 = "test-topic-3";

    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic testTopic1() {
        return new NewTopic(TOPIC_NAME_1, 3, (short) 3);
    }

    @Bean
    public NewTopic testTopic2() {
        return new NewTopic(TOPIC_NAME_2, 3, (short) 3);
    }

    @Bean
    public NewTopic testTopic3() {
        return new NewTopic(TOPIC_NAME_3, 3, (short) 3);
    }

}
