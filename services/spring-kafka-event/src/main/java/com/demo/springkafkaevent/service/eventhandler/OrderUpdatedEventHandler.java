package com.demo.springkafkaevent.service.eventhandler;

import com.demo.springkafkaevent.event.Event;
import com.demo.springkafkaevent.event.EventType;
import com.demo.springkafkaevent.event.payload.OrderCreatedEventPayload;
import com.demo.springkafkaevent.event.payload.OrderUpdatedEventPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderUpdatedEventHandler implements EventHandler<OrderUpdatedEventPayload> {
    @Override
    public void handle(Event<OrderUpdatedEventPayload> event) {
        OrderUpdatedEventPayload payload = event.payload();
        log.info("[OrderUpdatedEventHandler.handle] payload = {}", payload);
    }

    @Override
    public boolean supports(Event<OrderUpdatedEventPayload> event) {
        return EventType.ORDER_UPDATED == event.type();
    }
}
