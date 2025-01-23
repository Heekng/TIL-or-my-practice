package com.heekng.redisvariousways.service;

import com.heekng.redisvariousways.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class RateLimitServiceTest extends IntegrationTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private RateLimitService rateLimitService;

    @Test
    @DisplayName("이벤트 참여시 SET에 유저의 아이디가 추가된다.")
    void eventParticipateTest() throws Exception {
        // given
        // when
        Long result = rateLimitService.eventParticipate("0001", 38);

        // then
        Boolean keyExist = redisTemplate.opsForSet().isMember("event:0001", "38");

        assertThat(result).isEqualTo(1);
        assertThat(keyExist).isTrue();
    }

    @Test
    @DisplayName("특정 시간 이내에 이벤트 참여 불가 조건을 함께할 수 있다.")
    void eventParticipateWithExpireTest() throws Exception {
        // given
        // when
        Boolean result = rateLimitService.eventParticipateWithExpire("0001", 38, 100);

        // then
        Boolean keyExist = redisTemplate.hasKey("event:0001:38");
        Long expire = redisTemplate.getExpire("event:0001:38");

        // expected
        assertThat(result).isTrue();
        assertThat(keyExist).isTrue();
        assertThat(expire).isLessThan(101L);
    }

}