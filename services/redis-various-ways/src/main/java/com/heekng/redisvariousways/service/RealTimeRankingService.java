package com.heekng.redisvariousways.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RealTimeRankingService {

    private final RedisTemplate<String, String> redisTemplate;

    public Double putRankPoint(String rankTarget, String product) {
        return redisTemplate.opsForZSet().incrementScore("ranking:" + rankTarget, product, 1);
    }

    public Set<String> getRealtimeRankings(String rankTarget) {
        return redisTemplate.opsForZSet().reverseRange("ranking:" + rankTarget, 0, -1);
    }

    public Set<ZSetOperations.TypedTuple<String>> getRankingWithScore(String rankTarget) {
        return redisTemplate.opsForZSet().reverseRangeWithScores("ranking:" + rankTarget, 0, -1);
    }
}
