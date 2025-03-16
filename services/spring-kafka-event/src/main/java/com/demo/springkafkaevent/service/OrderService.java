package com.demo.springkafkaevent.service;

import com.demo.springkafkaevent.event.EventType;
import com.demo.springkafkaevent.event.payload.OrderCreatedEventPayload;
import com.demo.springkafkaevent.outboxmessagerelay.OutboxEventPublisher;
import com.demo.springkafkaevent.service.request.OrderCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OutboxEventPublisher outboxEventPublisher;

    @Transactional
    public void create(OrderCreateRequest request) {
        // order save 로직
        // ...

        outboxEventPublisher.publish(
                EventType.ORDER_CREATED,
                new OrderCreatedEventPayload(
                        request.orderId(),
                        request.ordererId(),
                        LocalDateTime.now()
                )
        );
    }
}
