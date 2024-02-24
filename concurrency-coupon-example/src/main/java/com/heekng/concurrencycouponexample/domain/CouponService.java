package com.heekng.concurrencycouponexample.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    @Transactional
    public void appendCouponUser(Long couponId, Long userId) {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new IllegalArgumentException("쿠폰을 찾을 수 없습니다."));
        coupon.appendCouponUser(userId);
    }
}
