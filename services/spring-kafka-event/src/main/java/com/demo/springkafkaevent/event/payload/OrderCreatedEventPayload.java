package com.demo.springkafkaevent.event.payload;

import com.demo.springkafkaevent.event.EventPayload;

import java.time.LocalDateTime;

public record OrderCreatedEventPayload(
        Long orderId,
        Long ordererId,
        LocalDateTime createdAt
) implements EventPayload {

    @Override
    public Long getEventId() {
        return orderId;
    }
}
