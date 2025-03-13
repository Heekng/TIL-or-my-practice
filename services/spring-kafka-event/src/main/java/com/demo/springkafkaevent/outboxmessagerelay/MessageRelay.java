package com.demo.springkafkaevent.outboxmessagerelay;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageRelay {
    private final EventOutboxRepository eventOutboxRepository;
    private final KafkaTemplate<String, String> messageRelayKafkaTemplate;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void createOutbox(OutboxEvent outboxEvent) {
        log.info("[MessageRelay.createOutbox] outboxEvent = {}", outboxEvent);
        eventOutboxRepository.save(outboxEvent.eventOutboxEntity());
    }

    @Async("messageRelayPublishEventExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishEvent(OutboxEvent outboxEvent) {
        publishEvent(outboxEvent.eventOutboxEntity());
    }

    @Scheduled(
            fixedDelay = 10,
            initialDelay = 5,
            timeUnit = TimeUnit.SECONDS,
            scheduler = "messageRelayPublishPendingEventScheduler"
    )
    public void publishPendingEvents() {
        eventOutboxRepository.findAllByCreatedAtLessThanEqualOrderByCreatedAtAsc(
                LocalDateTime.now().minusSeconds(10),
                Pageable.ofSize(100)
        ).forEach(this::publishEvent);
    }

    private void publishEvent(EventOutboxEntity eventOutboxEntity) {
        try {
            messageRelayKafkaTemplate.send(
                    eventOutboxEntity.getEventType().getTopic(),
                    String.valueOf(eventOutboxEntity.getEventId()),
                    eventOutboxEntity.getPayload()
            ).get(1, TimeUnit.SECONDS);
            eventOutboxRepository.delete(eventOutboxEntity);
        } catch (Exception e) {
            log.error("[MessageRelay.publishEvent] eventOutboxEntity = {}", eventOutboxEntity);
        }
    }
}
