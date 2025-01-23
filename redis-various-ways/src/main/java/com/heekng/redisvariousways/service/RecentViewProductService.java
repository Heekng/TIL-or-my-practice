package com.heekng.redisvariousways.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecentViewProductService {

    private final RedisTemplate<String, String> redisTemplate;

    public List<Object> addRecentView(String productId, String userId) {

        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        String key = "last-seen-product:" + userId;

        return redisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.multi();

                operations.opsForZSet().add(key, productId, Double.parseDouble(now));
                operations.opsForZSet().removeRange(key, 0, -6);

                return operations.exec();
            }
        });
    }
}
