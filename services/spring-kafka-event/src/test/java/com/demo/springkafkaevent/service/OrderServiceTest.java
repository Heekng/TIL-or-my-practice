package com.demo.springkafkaevent.service;

import com.demo.springkafkaevent.context.IntegrationTest;
import com.demo.springkafkaevent.event.EventType;
import com.demo.springkafkaevent.service.request.OrderCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

class OrderServiceTest extends IntegrationTest {

    @Autowired
    OrderService orderService;
    @MockBean
    KafkaTemplate<String, String> messageRelayKafkaTemplate;


    @Test
    @DisplayName("주문을 생성하면 ORDER 토픽에 이벤트가 발행된다.")
    void createTest() throws Exception {
        // given
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(1L, 2L);

        // when
        orderService.create(orderCreateRequest);

        // then
        Mockito.verify(messageRelayKafkaTemplate, Mockito.times(1)).send(anyString(), anyString(), anyString());
    }
}