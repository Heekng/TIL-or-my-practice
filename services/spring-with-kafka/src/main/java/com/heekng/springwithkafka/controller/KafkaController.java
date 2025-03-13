package com.heekng.springwithkafka.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.heekng.springwithkafka.dto.FirstDto;
import com.heekng.springwithkafka.service.EventListener;
import com.heekng.springwithkafka.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KafkaController {

    private final EventService eventService;

    @GetMapping("/send-message")
    public Boolean sendMessage(FirstDto firstDto) throws JsonProcessingException {
        eventService.registerEvent(firstDto);
        return true;
    }
}
