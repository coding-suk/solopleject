package org.personal.comerspleject.domain.coupon.service;

import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.config.exception.EcomosException;
import org.personal.comerspleject.config.exception.ErrorCode;
import org.personal.comerspleject.domain.coupon.entity.UserCoupon;
import org.personal.comerspleject.domain.coupon.repository.UserCouponRepository;
import org.personal.comerspleject.domain.users.user.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCouponService {

    private final UserCouponRepository userCouponRepository;

    public List<UserCoupon> getAvailableCoupons(User user) {
        return userCouponRepository.findByUserAndExpiredAtFalseAndUsedFalse(user);
    }

    public void useCoupon(Long userCouponId) {
        UserCoupon coupon = userCouponRepository.findById(userCouponId)
                .orElseThrow(() -> new EcomosException(ErrorCode._NOT_FOUND_COUPON));

        if (coupon.isUsed() || coupon.isExpired()) {
            throw new EcomosException(ErrorCode._INVALID_COUPON);
        }

        coupon.markAsUsed();
    }

    // 스케줄러 연동용, 만료 처리
    public void expireCoupon() {
        List<UserCoupon> toExpire = userCouponRepository.findCouponsToExpire(LocalDateTime.now());
        toExpire.forEach(UserCoupon::markAsExpired);
    }


}
