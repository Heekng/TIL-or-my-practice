package com.heekng.redisvariousways.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RateLimitService {

    private final RedisTemplate<String, String> redisTemplate;

    public Long eventParticipate(String eventId, long userId) {
        return redisTemplate.opsForSet()
                .add("event:" + eventId, String.valueOf(userId));
    }

    public Boolean eventParticipateWithExpire(String eventId, long userId, long expiresIn) {
        String key = "event:" + eventId + ":" + userId;
        Boolean result = redisTemplate.opsForValue()
                .setIfAbsent(key, "1");
        redisTemplate.expire(key, expiresIn, TimeUnit.SECONDS);
        return result;
    }


}
