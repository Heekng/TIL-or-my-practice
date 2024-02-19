package com.heekng.springwithkafka.service;

import com.heekng.springwithkafka.config.TopicConfig;
import com.heekng.springwithkafka.dto.FirstDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void FirstTopicProduce(final FirstDto firstDto) {
        String key = UUID.randomUUID().toString();
        kafkaTemplate.send(TopicConfig.TOPIC_NAME_1, key, firstDto).whenComplete((stringObjectSendResult, throwable) -> {
            if (throwable != null) {
                log.error("fail send message => {}", throwable.getMessage());
            } else {
                RecordMetadata metadata = stringObjectSendResult.getRecordMetadata();
                log.info("===SEND SUCCESS===");
                log.info("message: {}", firstDto);
                log.info("topic: {}", metadata.topic());
                log.info("partition: {}", metadata.partition());
                log.info("offset: {}", metadata.offset());
                log.info("==================");
            }
        });
    }

}
