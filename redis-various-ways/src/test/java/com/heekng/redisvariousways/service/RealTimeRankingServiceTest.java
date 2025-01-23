package com.heekng.redisvariousways.service;

import com.heekng.redisvariousways.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class RealTimeRankingServiceTest extends IntegrationTest {

    @Autowired
    private RealTimeRankingService realTimeRankingService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    @DisplayName("실시간 랭킹 등록을 할 수 있다.")
    void putRankPointTest() throws Exception {
        // given
        String product = "PRODUCT_001";

        // when
        Double result = realTimeRankingService.putRankPoint("test", product);

        // then
        assertThat(result).isEqualTo(1);
    }

    @Test
    @DisplayName("실시간 랭킹을 조회 할 수 있다.")
    void getRealtimeRankingsTest() throws Exception {
        // given
        String product1 = "PRODUCT_001";
        String product2 = "PRODUCT_002";
        String product3 = "PRODUCT_003";
        String product4 = "PRODUCT_004";

        redisTemplate.opsForZSet().incrementScore("ranking:test", product1, 1);
        redisTemplate.opsForZSet().incrementScore("ranking:test", product2, 1);
        redisTemplate.opsForZSet().incrementScore("ranking:test", product3, 1);
        redisTemplate.opsForZSet().incrementScore("ranking:test", product4, 1);
        redisTemplate.opsForZSet().incrementScore("ranking:test", product2, 1);
        redisTemplate.opsForZSet().incrementScore("ranking:test", product3, 1);
        redisTemplate.opsForZSet().incrementScore("ranking:test", product4, 1);
        redisTemplate.opsForZSet().incrementScore("ranking:test", product3, 1);
        redisTemplate.opsForZSet().incrementScore("ranking:test", product2, 1);
        redisTemplate.opsForZSet().incrementScore("ranking:test", product4, 1);
        redisTemplate.opsForZSet().incrementScore("ranking:test", product3, 1);
        redisTemplate.opsForZSet().incrementScore("ranking:test", product4, 1);
        redisTemplate.opsForZSet().incrementScore("ranking:test", product3, 1);
        // when
        Set<String> result = realTimeRankingService.getRealtimeRankings("test");

        // then
        assertThat(result).containsExactly(product3, product4, product2, product1);
    }

    @Test
    @DisplayName("실시간 랭킹과 점수를 함께 조회 할 수 있다.")
    void getRankingWithScoreTest() throws Exception {
        // given
        String product1 = "PRODUCT_001";
        String product2 = "PRODUCT_002";
        String product3 = "PRODUCT_003";
        String product4 = "PRODUCT_004";

        redisTemplate.opsForZSet().incrementScore("ranking:test", product1, 1);
        redisTemplate.opsForZSet().incrementScore("ranking:test", product2, 1);
        redisTemplate.opsForZSet().incrementScore("ranking:test", product3, 1);
        redisTemplate.opsForZSet().incrementScore("ranking:test", product4, 1);
        redisTemplate.opsForZSet().incrementScore("ranking:test", product2, 1);
        redisTemplate.opsForZSet().incrementScore("ranking:test", product3, 1);
        redisTemplate.opsForZSet().incrementScore("ranking:test", product4, 1);
        redisTemplate.opsForZSet().incrementScore("ranking:test", product3, 1);
        redisTemplate.opsForZSet().incrementScore("ranking:test", product2, 1);
        redisTemplate.opsForZSet().incrementScore("ranking:test", product4, 1);
        redisTemplate.opsForZSet().incrementScore("ranking:test", product3, 1);
        redisTemplate.opsForZSet().incrementScore("ranking:test", product4, 1);
        redisTemplate.opsForZSet().incrementScore("ranking:test", product3, 1);
        // when
        Set<ZSetOperations.TypedTuple<String>> result = realTimeRankingService.getRankingWithScore("test");

        // then
        assertThat(result)
                .extracting(ZSetOperations.TypedTuple::getValue, ZSetOperations.TypedTuple::getScore)
                .containsExactly(
                        tuple(product3, 5d),
                        tuple(product4, 4d),
                        tuple(product2, 3d),
                        tuple(product1, 1d)
                );
    }

}