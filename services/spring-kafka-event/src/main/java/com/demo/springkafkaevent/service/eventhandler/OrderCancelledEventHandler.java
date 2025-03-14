package com.demo.springkafkaevent.service.eventhandler;

import com.demo.springkafkaevent.event.Event;
import com.demo.springkafkaevent.event.EventType;
import com.demo.springkafkaevent.event.payload.OrderCancelledEventPayload;
import com.demo.springkafkaevent.event.payload.OrderUpdatedEventPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderCancelledEventHandler implements EventHandler<OrderCancelledEventPayload> {
    @Override
    public void handle(Event<OrderCancelledEventPayload> event) {
        OrderCancelledEventPayload payload = event.payload();
        log.info("[OrderCancelledEventHandler.handle] payload = {}", payload);
    }

    @Override
    public boolean supports(Event<OrderCancelledEventPayload> event) {
        return EventType.ORDER_CANCELLED == event.type();
    }
}
