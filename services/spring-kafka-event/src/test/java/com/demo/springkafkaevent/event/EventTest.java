package com.demo.springkafkaevent.event;

import com.demo.springkafkaevent.event.payload.OrderCreatedEventPayload;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class EventTest {
    @Test
    @DisplayName("eventType과 eventPayload로 Event를 생성할 수 있다.")
    void ofTest() throws Exception {
        // given
        EventType eventType = EventType.ORDER_CREATED;
        OrderCreatedEventPayload payload = new OrderCreatedEventPayload(1L, 1L, LocalDateTime.now());

        // when
        Event<EventPayload> result = Event.of(eventType, payload);

        // then
        assertThat(result.eventKey()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Event를 json으로 변환할 수 있다.")
    void toJsonTest() throws Exception {
        // given
        EventType eventType = EventType.ORDER_CREATED;
        OrderCreatedEventPayload payload = new OrderCreatedEventPayload(1L, 1L, LocalDateTime.now());
        Event<EventPayload> event = Event.of(eventType, payload);

        // when
        String result = event.toJson();

        // then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("json으로 Event를 생성할 수 있다.")
    void fromJsonTest() throws Exception {
        // given
        String json = "{\"eventKey\":1,\"type\":\"ORDER_CREATED\",\"payload\":{\"orderId\":1,\"ordererId\":1,\"createdAt\":[2025,3,16,13,17,34,641634000],\"eventKey\":1}}";

        // when
        Event<EventPayload> result = Event.fromJson(json);

        // then
        assertThat(result.eventKey()).isEqualTo(1L);
        assertThat(result.type()).isEqualTo(EventType.ORDER_CREATED);
        EventPayload payload = result.payload();
        assertThat(payload.getEventId()).isEqualTo(1L);
    }
}