package com.heekng.concurrencycouponexample.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponUserRepository extends JpaRepository<CouponUser, Long> {}
