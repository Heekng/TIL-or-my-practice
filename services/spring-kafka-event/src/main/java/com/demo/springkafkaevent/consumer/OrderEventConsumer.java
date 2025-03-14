package com.demo.springkafkaevent.consumer;

import com.demo.springkafkaevent.event.Event;
import com.demo.springkafkaevent.event.EventType;
import com.demo.springkafkaevent.service.ProductionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventConsumer {
    private final ProductionService productionService;

    @KafkaListener(topics = {
            EventType.Topic.ORDER
    })
    public void listen(String message, Acknowledgment ack) {
        log.info("[OrderEventConsumer.listen] message = {}", message);
        Optional.ofNullable(Event.fromJson(message))
                .ifPresent(productionService::handleEvent);
        ack.acknowledge();
    }
}
