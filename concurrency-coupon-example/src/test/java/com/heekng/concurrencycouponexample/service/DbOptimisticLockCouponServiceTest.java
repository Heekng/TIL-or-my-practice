package com.heekng.concurrencycouponexample.service;

import com.heekng.concurrencycouponexample.domain.Coupon;
import com.heekng.concurrencycouponexample.domain.CouponRepository;
import com.heekng.concurrencycouponexample.domain.CouponUser;
import com.heekng.concurrencycouponexample.domain.CouponUserRepository;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DbOptimisticLockCouponServiceTest {

    @Autowired
    private DbOptimisticLockCouponService couponService;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private CouponUserRepository couponUserRepository;

    @AfterEach
    void tearDown() {
        couponUserRepository.deleteAllInBatch();
        couponRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("OptimisticLock을 이용한 동시성 해결")
    void OptimisticLock을_이용한_동시성_해결() throws Exception {
        // given
        Coupon coupon = couponRepository.saveAndFlush(new Coupon("COUPON_1", 100L));

        int numberOfThread = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThread);
        CountDownLatch latch = new CountDownLatch(numberOfThread);
        // when
        for (int i = 0; i < numberOfThread; i++) {
            long currentUserId = i;
            executorService.submit(() -> {
                try {
                    couponService.appendCouponUser(coupon.getId(), currentUserId);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        // expected
        Coupon findedCoupon = couponRepository.findById(coupon.getId()).orElseThrow();
        List<CouponUser> couponUsers = couponUserRepository.findAll();
        Assertions.assertThat(findedCoupon.getQuantity()).isEqualTo(0L);
        Assertions.assertThat(couponUsers.size()).isEqualTo(100);
    }

}
