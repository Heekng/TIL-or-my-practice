package com.heekng.springwithkafka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heekng.springwithkafka.config.KafkaTopicConfig;
import com.heekng.springwithkafka.dto.FirstDto;
import com.heekng.springwithkafka.dto.FirstTopicEvent;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventListener {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void FirstTopicProduce(final FirstTopicEvent event) throws JsonProcessingException {
        String key = UUID.randomUUID().toString();
        kafkaTemplate.send(KafkaTopicConfig.TOPIC_NAME_1, key, objectMapper.writeValueAsString(event))
            .whenComplete((stringObjectSendResult, throwable) -> {
            if (throwable != null) {
                log.error("fail send message => {}", throwable.getMessage());
                // 이벤트 발행 실패 노티
                // 이벤트 발행 실패 DB insert
            } else {
                RecordMetadata metadata = stringObjectSendResult.getRecordMetadata();
                log.info("===SEND SUCCESS===");
                log.info("message: {}", stringObjectSendResult.getProducerRecord().value());
                log.info("topic: {}", metadata.topic());
                log.info("partition: {}", metadata.partition());
                log.info("offset: {}", metadata.offset());
                log.info("==================");
            }
        });
    }

}
