package com.heekng.redisvariousways.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThumbsUpService {

    private final RedisTemplate<String, String> redisTemplate;

    public Long thumbsUp(Long postId, String memberId) {
        String key = "post:" + postId + ":like";
        return redisTemplate.opsForSet().add(key, memberId);
    }

    public Long cancle(Long postId, String memberId) {
        String key = "post:" + postId + ":like";
        return redisTemplate.opsForSet().remove(key, memberId);
    }

    public Long count(Long postId) {
        String key = "post:" + postId + ":like";
        return redisTemplate.opsForSet().size(key);
    }

    public Boolean exists(Long postId, String memberId) {
        String key = "post:" + postId + ":like";
        return redisTemplate.opsForSet().isMember(key, memberId);
    }

    public List<Object> thumbsUpBetween(Long postId, String memberId, Boolean error) {
        Boolean postLike = redisTemplate.opsForSet().isMember("post:" + postId + ":like", memberId);
        Boolean memberPostLike = redisTemplate.opsForSet().isMember("member:" + memberId + ":like:post", postId.toString());

        if (postLike && memberPostLike) {
            throw new IllegalStateException("이미 좋아요 한 게시물입니다.");
        }

        List<Object> results = redisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                operations.opsForSet().add("post:" + postId + ":like", memberId);
                operations.opsForSet().add("member:" + memberId + ":like:post", String.valueOf(postId));

                if (error) {
                    throw new IllegalAccessError("트랜잭션 내에서 에러가 발생했습니다.");
                }

                return operations.exec();
            }
        });
        return results;
    }



}
