package com.heekng.springwithkafka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.heekng.springwithkafka.dto.FirstDto;
import com.heekng.springwithkafka.dto.FirstTopicEvent;
import com.heekng.springwithkafka.dto.FirstTopicEvent.FirstInnerEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventService {

    private final ApplicationEventPublisher eventPublisher;
    private final EventListener eventListener;

    @Transactional
    public boolean registerEvent(FirstDto firstDto) throws JsonProcessingException {
        // 이벤트 참여 로직
        FirstTopicEvent firstTopicEvent = new FirstTopicEvent(firstDto.id(), firstDto.name(),
            firstDto.age(),
            new FirstInnerEvent(firstDto.firstInnerDto().innerName(),
                firstDto.firstInnerDto().length()));
        eventListener.FirstTopicProduce(firstTopicEvent);
//        throw new IllegalStateException();
        return true;
    }

}
