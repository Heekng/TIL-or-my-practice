package com.heekng.redisconcurrencylock.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.pubsub.CountDownLatchPubSub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CouponDecreaseServiceTest {

    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private CouponDecreaseService couponDecreaseService;

    @AfterEach
    void tearDown() {
        couponRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("999개의 쿠폰을 동시에 발급 시도하면 분산락이 적용되어 재고를 감소시킨다.")
    void decreaseCouponTest() throws InterruptedException {
        // given
        Coupon coupon = new Coupon("EVENT_1_COUPON", 999L);
        couponRepository.save(coupon);

        // when
        int numberOfThreads = 999;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    couponDecreaseService.decreaseCoupon(coupon.getId());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        // then
        Coupon findedCoupon = couponRepository.findById(coupon.getId()).orElseThrow(() -> new IllegalArgumentException("쿠폰이 존재하지 않습니다."));
        Assertions.assertThat(findedCoupon.getStock()).isZero();
    }

}
