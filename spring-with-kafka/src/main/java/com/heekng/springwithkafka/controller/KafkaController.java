package com.heekng.springwithkafka.controller;

import com.heekng.springwithkafka.dto.FirstDto;
import com.heekng.springwithkafka.service.ProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KafkaController {

    private final ProducerService producerService;

    @GetMapping("/send-message")
    public Boolean sendMessage(FirstDto firstDto) {
        producerService.FirstTopicProduce(firstDto);
        return true;
    }
}
