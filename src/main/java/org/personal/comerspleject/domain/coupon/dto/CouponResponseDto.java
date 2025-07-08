package org.personal.comerspleject.domain.coupon.dto;

import lombok.Getter;
import org.personal.comerspleject.domain.coupon.entity.Coupon;
import org.personal.comerspleject.domain.coupon.entity.CouponType;

import java.time.LocalDateTime;

@Getter
public class CouponResponseDto {

    private Long cId;
    private String name;
    private int discountAmount;
    private boolean isPercent;
    private int minOrderAmount;
    private CouponType type;
    private LocalDateTime expiredAt;

    public CouponResponseDto(Long cId, String name, int discountAmount,
                             boolean isPercent, int minOrderAmount,
                             CouponType type, LocalDateTime expiredAt) {
        this.cId = cId;
        this.name = name;
        this.discountAmount = discountAmount;
        this.isPercent = isPercent;
        this.minOrderAmount = minOrderAmount;
        this.type = type;
        this.expiredAt = expiredAt;

    }

    public static CouponResponseDto from(Coupon coupon) {
        return new CouponResponseDto(
                coupon.getCId(),
                coupon.getName(),
                coupon.getDiscountAmount(),
                coupon.isPercent(),
                coupon.getMinOrderAmount(),
                coupon.getType(),
                coupon.getExpiredAt());

    }

}
