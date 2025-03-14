package com.demo.springkafkaevent.service.eventhandler;

import com.demo.springkafkaevent.event.Event;
import com.demo.springkafkaevent.event.EventType;
import com.demo.springkafkaevent.event.payload.OrderCreatedEventPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderCreatedEventHandler implements EventHandler<OrderCreatedEventPayload> {
    @Override
    public void handle(Event<OrderCreatedEventPayload> event) {
        OrderCreatedEventPayload payload = event.payload();
        log.info("[OrderCreatedEventHandler.handle] payload = {}", payload);
    }

    @Override
    public boolean supports(Event<OrderCreatedEventPayload> event) {
        return EventType.ORDER_CREATED == event.type();
    }
}
