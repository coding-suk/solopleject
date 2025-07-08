package org.personal.comerspleject.domain.coupon.dto;

import lombok.Getter;
import org.personal.comerspleject.domain.coupon.entity.UserCoupon;

import java.time.LocalDateTime;

@Getter
public class UserCouponResponseDto {

    private Long ucId;
    private String couponName;
    private boolean used;
    private boolean expired;
    private LocalDateTime expiredAt;

    public UserCouponResponseDto(Long ucId, String couponName, boolean used,
                                 boolean expired, LocalDateTime expiredAt) {
        this.ucId = ucId;
        this.couponName = couponName;
        this.used = used;
        this.expired = expired;
        this.expiredAt = expiredAt;
    }

    public static UserCouponResponseDto from(UserCoupon uc) {
        return new UserCouponResponseDto(
                uc.getUcId(),
                uc.getCoupon().getName(),
                uc.isUsed(),
                uc.isExpired(),
                uc.getExpiredAt());

    }

}
