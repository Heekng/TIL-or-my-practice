package com.heekng.springwithkafka.service;

import com.heekng.springwithkafka.config.TopicConfig;
import com.heekng.springwithkafka.dto.FirstDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerService {

    @KafkaListener(topics = TopicConfig.TOPIC_NAME_1, groupId = "kafka-consumer-1")
    public void topic1Listen(FirstDto firstDto, ConsumerRecordMetadata metadata) {
        log.info("===RECEIVE SUCCESS===");
        log.info("message: {}", firstDto);
        log.info("topic: {}", metadata.topic());
        log.info("partition: {}", metadata.partition());
        log.info("offset: {}", metadata.offset());
        log.info("==================");
    }

}

