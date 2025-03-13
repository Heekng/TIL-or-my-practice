package com.demo.springkafkaevent.outboxmessagerelay;

public record OutboxEvent(
        EventOutboxEntity eventOutboxEntity
) {
}
