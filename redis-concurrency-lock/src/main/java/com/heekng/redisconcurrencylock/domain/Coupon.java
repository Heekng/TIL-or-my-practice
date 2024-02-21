package com.heekng.redisconcurrencylock.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private String name;
    private Long stock;

    public Coupon(String name, Long stock) {
        this.name = name;
        this.stock = stock;
    }

    public void decreaseStock() {
        validateStockCount();
        this.stock -= 1;
    }

    private void validateStockCount() {
        if (stock < 1L) {
            throw new IllegalArgumentException("수량이 1보다 작습니다.");
        }
    }

}
