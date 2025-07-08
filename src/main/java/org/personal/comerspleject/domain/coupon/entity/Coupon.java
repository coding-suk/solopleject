package org.personal.comerspleject.domain.coupon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// 쿠폰의 마스터 정보(전체 발급 조전, 할인율등)
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cId;

    private String name; // 신규 가입 시 10% 할일
    private int discountAmount; // 정액(고정금액), 정률(퍼센트 할일) 금액(선택)
    private int minOrderAmount; // 최소 주문 금액
    private boolean isPercent; // 퍼샌트 여부

    @Enumerated(EnumType.STRING)
    private CouponType type;

    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;

    public Coupon(String name, int discountAmount, int minOrderAmount,
                  boolean isPercent, CouponType type, LocalDateTime expiredAt) {
        this.name = name;
        this.discountAmount = discountAmount;
        this.minOrderAmount = minOrderAmount;
        this.isPercent = isPercent;
        this.type = type;
        this.createdAt = LocalDateTime.now();
        this.expiredAt = expiredAt;
    }

}
