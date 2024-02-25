package com.heekng.concurrencycouponexample.service;import com.heekng.concurrencycouponexample.domain.CouponService;import lombok.RequiredArgsConstructor;import org.springframework.stereotype.Service;/** *  비관적 락 *  SELECT FROM WHERE for update 문을 통해 조회된 데이터에 락을 건다. *  별도의 락을 걸기 떄문에 성능 감소가 일어날 수 있다. */@Service@RequiredArgsConstructorpublic class PessimisticLockCouponService {    private final CouponService couponService;    public void appendCouponUser(Long couponId, Long userId) {        couponService.appendCouponUserPessimistic(couponId, userId);    }}