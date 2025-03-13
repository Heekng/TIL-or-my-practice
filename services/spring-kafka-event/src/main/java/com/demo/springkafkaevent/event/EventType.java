package com.demo.springkafkaevent.event;

import com.demo.springkafkaevent.event.payload.OrderCancelledEventPayload;
import com.demo.springkafkaevent.event.payload.OrderCreatedEventPayload;
import com.demo.springkafkaevent.event.payload.OrderUpdatedEventPayload;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum EventType {
    ORDER_CREATED(OrderCreatedEventPayload.class, Topic.ORDER),
    ORDER_UPDATED(OrderUpdatedEventPayload.class, Topic.ORDER),
    ORDER_CANCELLED(OrderCancelledEventPayload.class, Topic.ORDER),
    ;

    private final Class<? extends EventPayload> payloadClass;
    private final String topic;


    public static EventType from(String type) {
        try {
            return valueOf(type);
        } catch (Exception e) {
            log.error("[EventType.from] type = {}", type);
            return null;
        }
    }

    public static class Topic {
        public static final String ORDER = "order";
    }

}
