package com.heekng.redisvariousways.service;

import com.heekng.redisvariousways.IntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class RecentViewProductServiceTest extends IntegrationTest {

    @Autowired
    private RecentViewProductService recentViewProductService;
    @Autowired
    private RedisTemplate redisTemplate;

    @AfterEach
    void tearDown() {
        redisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll();
    }

    @Test
    @DisplayName("최근 본 상품을 추가하고, 최근 본 상품 5개만 유지한다.")
    void addRecentView() throws Exception {
        // given
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        redisTemplate.opsForZSet().add("last-seen-product:user1", "product1",
                Double.parseDouble(
                        LocalDateTime.of(2025, 1, 21, 23, 10).format(dateTimeFormatter)
                ));
        redisTemplate.opsForZSet().add("last-seen-product:user1", "product2",
                Double.parseDouble(
                        LocalDateTime.of(2025, 1, 21, 23, 14).format(dateTimeFormatter)
                ));
        redisTemplate.opsForZSet().add("last-seen-product:user1", "product3",
                Double.parseDouble(
                        LocalDateTime.of(2025, 1, 21, 23, 20).format(dateTimeFormatter)
                ));
        redisTemplate.opsForZSet().add("last-seen-product:user1", "product4",
                Double.parseDouble(
                        LocalDateTime.of(2025, 1, 21, 23, 30).format(dateTimeFormatter)
                ));
        redisTemplate.opsForZSet().add("last-seen-product:user1", "product5",
                Double.parseDouble(
                        LocalDateTime.of(2025, 1, 21, 23, 32).format(dateTimeFormatter)
                ));
        redisTemplate.opsForZSet().add("last-seen-product:user1", "product6",
                Double.parseDouble(
                        LocalDateTime.of(2025, 1, 21, 23, 33).format(dateTimeFormatter)
                ));
        // when
        List<Object> results = recentViewProductService.addRecentView("product7", "user1");

        // then
        Set lastSeenProducts = redisTemplate.opsForZSet().reverseRange("last-seen-product:user1", 0, -1);
        assertThat(results).containsExactly(true, 2L);
        assertThat(lastSeenProducts).containsExactly("product7", "product6", "product5", "product4", "product3");
    }
}