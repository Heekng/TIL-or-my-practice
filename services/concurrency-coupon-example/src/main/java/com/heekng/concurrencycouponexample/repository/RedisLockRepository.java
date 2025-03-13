package com.heekng.concurrencycouponexample.repository;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisLockRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public Boolean getLock(String key) {
        return redisTemplate.opsForValue().setIfAbsent(key, "lock", Duration.ofMillis(3000));
    }

    public Boolean unLock(String key) {
        return redisTemplate.delete(key);
    }

}
