package com.heekng.springwithkafka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heekng.springwithkafka.config.KafkaTopicConfig;
import com.heekng.springwithkafka.dto.FirstDto;
import com.heekng.springwithkafka.dto.FirstTopicEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerService {

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = KafkaTopicConfig.TOPIC_NAME_1, containerFactory = "testTopic1kafkaListenerContainerFactory")
    public void topic1Listener(ConsumerRecord<String, String> consumerRecord)
        throws JsonProcessingException {
        FirstTopicEvent firstTopicEvent = objectMapper.readValue(consumerRecord.value(), FirstTopicEvent.class);
        log.info("===RECEIVE SUCCESS===");
        log.info("message: {}", firstTopicEvent);
        log.info("topic: {}", consumerRecord.topic());
        log.info("partition: {}", consumerRecord.partition());
        log.info("offset: {}", consumerRecord.offset());
        log.info("==================");
    }
}

