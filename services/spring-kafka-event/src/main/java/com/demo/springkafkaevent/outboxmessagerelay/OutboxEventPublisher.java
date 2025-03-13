package com.demo.springkafkaevent.outboxmessagerelay;

import com.demo.springkafkaevent.event.Event;
import com.demo.springkafkaevent.event.EventPayload;
import com.demo.springkafkaevent.event.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public void publish(EventType type, EventPayload payload) {
        EventOutboxEntity eventOutboxEntity = EventOutboxEntity.create(
                type,
                Event.of(
                        type,
                        payload
                ).toJson(),
                payload.getEventId()
        );
        applicationEventPublisher.publishEvent(new OutboxEvent(eventOutboxEntity));
    }
}
