package com.demo.springkafkaevent.service;

import com.demo.springkafkaevent.event.Event;
import com.demo.springkafkaevent.event.EventPayload;
import com.demo.springkafkaevent.service.eventhandler.EventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductionService {
    private final List<EventHandler> eventHandlers;

    public void handleEvent(Event<EventPayload> event) {
        eventHandlers.stream()
                .filter(handler -> handler.supports(event))
                .peek(handler -> handler.handle(event));
    }
}
