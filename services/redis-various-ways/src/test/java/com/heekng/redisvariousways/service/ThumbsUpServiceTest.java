package com.heekng.redisvariousways.service;

import com.heekng.redisvariousways.IntegrationTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ThumbsUpServiceTest extends IntegrationTest {

    @Autowired
    private ThumbsUpService thumbsUpService;
    @Autowired
    private RedisTemplate redisTemplate;

    @AfterEach
    void tearDown() {
        redisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll();
    }

    @Test
    @DisplayName("게시물에 좋아요를 할 수 있다.")
    void thumbsUpTest() throws Exception {
        // given
        Long postId = 1L;
        String userId = "USER_1";

        // when
        Long result = thumbsUpService.thumbsUp(postId, userId);

        // then
        Boolean isMember = redisTemplate.opsForSet().isMember("post:" + postId + ":like", userId);
        assertThat(result).isEqualTo(1L);
        assertThat(isMember).isTrue();
    }

    @Test
    @DisplayName("게시물 좋아요를 취소할 수 있다.")
    void cancleTest() throws Exception {
        // given
        Long postId = 1L;
        String userId = "USER_1";
        String key = "post:" + postId + ":like";
        redisTemplate.opsForSet().add(key, userId);

        // when
        Long result = thumbsUpService.cancle(postId, userId);

        // then
        Boolean isExist = redisTemplate.opsForSet().isMember(key, userId);
        assertThat(result).isEqualTo(1);
        assertThat(isExist).isFalse();
    }

    @Test
    @DisplayName("게시물의 좋아요 수를 확인할 수 있다.")
    void countTest() throws Exception {
        // given
        Long postId = 1L;
        String key = "post:" + postId + ":like";
        String userId1 = "USER_1";
        String userId2 = "USER_2";
        String userId3 = "USER_3";
        String userId4 = "USER_4";
        redisTemplate.opsForSet().add(key, userId1);
        redisTemplate.opsForSet().add(key, userId2);
        redisTemplate.opsForSet().add(key, userId3);
        redisTemplate.opsForSet().add(key, userId4);

        // when
        Long result = thumbsUpService.count(postId);

        // then
        assertThat(result).isEqualTo(4);
    }
    
    @Test
    @DisplayName("사용자가 게시물에 좋아요했는지 확인할 수 있다.")
    void existsTest() throws Exception {
        // given
        Long postId = 1L;
        String key = "post:" + postId + ":like";
        String userId1 = "USER_1";
        redisTemplate.opsForSet().add(key, userId1);

        // when
        Boolean result = thumbsUpService.exists(postId, userId1);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("사용자와 게시물 양쪽에 좋아요를 할 수 있다.")
    void thumbsUpBetweenTest() throws Exception {
        // given
        Long postId = 1L;
        String memberId = "USER_1";

        // when
        List<Object> results = thumbsUpService.thumbsUpBetween(postId, memberId, false);

        // then
        Boolean postLikeExist = redisTemplate.opsForSet().isMember("post:" + postId + ":like", memberId);
        Boolean userLikeExist = redisTemplate.opsForSet().isMember("member:" + memberId + ":like:post", postId.toString());
        assertThat(results).containsExactly(1L, 1L);
        assertThat(postLikeExist).isTrue();
        assertThat(userLikeExist).isTrue();
    }
}