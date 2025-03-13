package com.demo.springkafkaevent.event.payload;

import com.demo.springkafkaevent.event.EventPayload;

import java.time.LocalDateTime;

public record OrderUpdatedEventPayload(
        Long orderId,
        Long ordererId,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) implements EventPayload {
    @Override
    public Long getEventId() {
        return orderId;
    }
}
