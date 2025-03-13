package com.heekng.springwithkafka.config;

import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.CommonLoggingErrorHandler;

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    private final KafkaProperties kafkaProperties;

    @Bean
    public ConsumerFactory<String, String> testTopic1ConsumerFactory() {
        return setConsumerFactory(kafkaProperties.consumer());
    }

    private ConsumerFactory<String, String> setConsumerFactory(final KafkaProperties.Consumer consumer) {
        HashMap<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumer.bootstrapServers());
        configProps.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, consumer.timeoutMs());
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, consumer.groupId());
        configProps.put(ConsumerConfig.CLIENT_ID_CONFIG, consumer.clientId());
        configProps.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, consumer.fetchMinBytes());
        configProps.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, consumer.fetchMaxWaitMs());
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, consumer.autoOffsetReset());
        configProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, consumer.maxPollRecords());
        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, consumer.enableAutoCommit());
        configProps.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");

        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> testTopic1kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(testTopic1ConsumerFactory());
        factory.setCommonErrorHandler(new CommonLoggingErrorHandler());
        return factory;
    }

}
