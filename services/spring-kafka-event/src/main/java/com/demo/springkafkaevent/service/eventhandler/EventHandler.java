package com.demo.springkafkaevent.service.eventhandler;

import com.demo.springkafkaevent.event.Event;
import com.demo.springkafkaevent.event.EventPayload;

public interface EventHandler<T extends EventPayload> {
    void handle(Event<T> event);

    boolean supports(Event<T> event);
}
