package com.heekng.concurrencycouponexample.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String couponCode;
    private Long quantity;

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL)
    private List<CouponUser> couponUsers = new ArrayList<>();

    @Version
    private Long version;

    public Coupon(String couponCode, Long quantity) {
        this.couponCode = couponCode;
        this.quantity = quantity;
    }

    public void appendCouponUser(Long userId) {
        decreaseCoupon();
        CouponUser couponUser = new CouponUser(this, userId);
        this.couponUsers.add(couponUser);
    }
    private void decreaseCoupon() {
        if (this.quantity < 1) {
            throw new IllegalStateException("쿠폰 수량이 부족합니다.");
        }
        this.quantity -= 1;
    }
}
