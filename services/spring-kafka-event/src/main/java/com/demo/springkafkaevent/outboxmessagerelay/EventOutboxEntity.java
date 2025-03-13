package com.demo.springkafkaevent.outboxmessagerelay;

import com.demo.springkafkaevent.event.EventType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Table(name = "event_outbox")
@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventOutboxEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventOutboxId;
    @Enumerated(EnumType.STRING)
    private EventType eventType;
    private String payload;
    private Long eventId;
    private LocalDateTime createdAt;

    public static EventOutboxEntity create(EventType eventType, String payload, Long eventId) {
        EventOutboxEntity eventOutboxEntity = new EventOutboxEntity();
        eventOutboxEntity.eventType = eventType;
        eventOutboxEntity.payload = payload;
        eventOutboxEntity.eventId = eventId;
        eventOutboxEntity.createdAt = LocalDateTime.now();
        return eventOutboxEntity;
    }

}
