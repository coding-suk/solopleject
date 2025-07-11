package org.personal.comerspleject.domain.policyAndscheduler;

import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.config.exception.EcomosException;
import org.personal.comerspleject.config.exception.ErrorCode;
import org.personal.comerspleject.domain.coupon.entity.Coupon;
import org.personal.comerspleject.domain.coupon.entity.UserCoupon;
import org.personal.comerspleject.domain.coupon.repository.CouponRepository;
import org.personal.comerspleject.domain.coupon.repository.UserCouponRepository;
import org.personal.comerspleject.domain.order.repository.OrderRepository;
import org.personal.comerspleject.domain.users.user.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class FirstOrderCouponPolicy implements CouponPolicy{

    private static final String COUPON_NAME = "첫 구매 축하 쿠폰";

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final OrderRepository orderRepository;

    @Override
    public boolean supports(User user) {
        // 첫 주문 완료 여부
        long orderCount = orderRepository.countByUser(user);
        return orderCount == 1;
    }

    @Override
    @Transactional
    public void issue(User user) {
        // 이미 해당 쿠폰을 발급받았는지 확인
        boolean alreadyIssued = userCouponRepository.existsByUserAndCoupon_Name(user, COUPON_NAME);
        if (alreadyIssued) return;

        Coupon firstOrderCoupon = couponRepository.findByName(COUPON_NAME)
                .orElseThrow(() -> new EcomosException(ErrorCode._NOT_FOUND_COUPON));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiredAt = now.plusDays(7); // 유효기간 7일

        UserCoupon userCoupon = new UserCoupon(user, firstOrderCoupon, now, expiredAt);
        userCouponRepository.save(userCoupon);
    }

}
