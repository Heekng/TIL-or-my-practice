package com.demo.springkafkaevent.event;

import com.demo.springkafkaevent.util.DataSerializer;

import java.util.Optional;

public record Event<T extends EventPayload>(
        Long eventId,
        EventType eventType,
        T payload
) {
    public static Event<EventPayload> of(EventType type, EventPayload payload) {
        return new Event<>(
                payload.getEventId(),
                type,
                payload
        );
    }

    public String toJson() {
        return DataSerializer.serialize(this);
    }

    public static Event<EventPayload> fromJson(String json) {
        EventRaw eventRaw = DataSerializer.deserialize(json, EventRaw.class);
        if (eventRaw == null) {
            return null;
        }

        EventType type = EventType.from(eventRaw.type);
        EventPayload payload = DataSerializer.deserialize(eventRaw.payload, type.getPayloadClass());

        return new Event<>(
                eventRaw.eventKey,
                type,
                payload
        );
    }

    private record EventRaw(
            Long eventKey,
            String type,
            Object payload
    ) {}
}
