package com.demo.springkafkaevent.service.request;

public record OrderCreateRequest(
        Long orderId,
        Long ordererId
) {}
