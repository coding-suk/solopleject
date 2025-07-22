package org.personal.comerspleject.domain.coupon.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.personal.comerspleject.config.exception.EcomosException;
import org.personal.comerspleject.config.exception.ErrorCode;
import org.personal.comerspleject.domain.coupon.entity.UserCoupon;
import org.personal.comerspleject.domain.coupon.repository.UserCouponRepository;
import org.personal.comerspleject.domain.users.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCouponService {

    private final UserCouponRepository userCouponRepository;

    public List<UserCoupon> getAvailableCoupons(User user) {
        return userCouponRepository.findByUserAndExpiredFalseAndUsedFalse(user);
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
        log.info("만료된 쿠폰 수 : "+toExpire.size());
    }

    @Transactional
    public void notifyCouponsExpiringTomorrow() {

        LocalDateTime start = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime end = start.plusDays(1).minusNanos(1);

        List<UserCoupon> expiring = userCouponRepository.findCouponsExpiringTomorrow(start, end);

        for(UserCoupon uc : expiring) {
            log.info("[쿠폰 소멸 알림] 유저: {}, 쿠폰: {}, 만료일: {}",
                    uc.getUser().getEmail(),
                    uc.getCoupon().getName(),
                    uc.getExpiredAt());
        }

    }


}
