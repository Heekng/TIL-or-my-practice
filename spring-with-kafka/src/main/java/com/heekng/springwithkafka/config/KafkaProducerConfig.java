package com.heekng.springwithkafka.config;

import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {

    private final KafkaProperties kafkaProperties;

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return setProducerFactory(kafkaProperties.producer());
    }

    private ProducerFactory<String, String> setProducerFactory(final KafkaProperties.Producer producer) {
        HashMap<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producer.bootstrapServers());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configProps.put(ProducerConfig.CLIENT_ID_CONFIG, producer.clientId());
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, producer.enableIdempotence());
        configProps.put(ProducerConfig.ACKS_CONFIG, producer.acks());
        configProps.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, producer.maxInFlightRequestsPerConnection());
        configProps.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "TEST_TRANSACTION");
        configProps.put("spring.kafka.admin.properties.min.insync.replicas", producer.minInSyncReplicas());

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
