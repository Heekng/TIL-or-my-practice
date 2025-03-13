package com.heekng.redisconcurrencylock.domain;

import com.heekng.redisconcurrencylock.annotation.DistributedLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponDecreaseService {

    private final CouponRepository couponRepository;

    @Transactional
    @DistributedLock(key = "'coupon'.concat('-').concat(#couponId)")
    public void decreaseCoupon(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new IllegalArgumentException("쿠폰이 존재하지 않습니다."));
        coupon.decreaseStock();
        log.info("쿠폰 재고 감소 성공");
    }

}
