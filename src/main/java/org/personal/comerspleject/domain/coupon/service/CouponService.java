package org.personal.comerspleject.domain.coupon.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.LifecycleState;
import org.personal.comerspleject.config.exception.EcomosException;
import org.personal.comerspleject.config.exception.ErrorCode;
import org.personal.comerspleject.domain.coupon.dto.CouponResponseDto;
import org.personal.comerspleject.domain.coupon.entity.Coupon;
import org.personal.comerspleject.domain.coupon.entity.UserCoupon;
import org.personal.comerspleject.domain.coupon.repository.CouponRepository;
import org.personal.comerspleject.domain.coupon.repository.UserCouponRepository;
import org.personal.comerspleject.domain.users.user.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Service
@RequiredArgsConstructor
public class CouponService {

    private final UserCouponRepository userCouponRepository;
    private final CouponRepository couponRepository;

    public List<CouponResponseDto> getAllCoupons() {
        return couponRepository.findAll()
                .stream()
                .map(CouponResponseDto::from)
                .toList();
    }

    public CouponResponseDto getCouponDetail(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new EcomosException(ErrorCode._NOT_FOUND_COUPON));
        return CouponResponseDto.from(coupon);
    }

    public void issueCouponToUser(User user, Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new EcomosException(ErrorCode._NOT_FOUND_COUPON));

        UserCoupon userCoupon = new UserCoupon(user, coupon, LocalDateTime.now(), coupon.getExpiredAt());
        userCouponRepository.save(userCoupon);
    }

}
