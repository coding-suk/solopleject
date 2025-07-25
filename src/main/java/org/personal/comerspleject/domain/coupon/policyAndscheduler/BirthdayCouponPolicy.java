package org.personal.comerspleject.domain.coupon.policyAndscheduler;

import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.config.exception.EcomosException;
import org.personal.comerspleject.config.exception.ErrorCode;
import org.personal.comerspleject.domain.coupon.entity.Coupon;
import org.personal.comerspleject.domain.coupon.entity.UserCoupon;
import org.personal.comerspleject.domain.coupon.repository.CouponRepository;
import org.personal.comerspleject.domain.coupon.repository.UserCouponRepository;
import org.personal.comerspleject.domain.users.user.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class BirthdayCouponPolicy implements CouponPolicy{

    private static final String COUPON_NAME = "생일 축하 쿠폰";

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    @Override
    public boolean supports(User user) {

        LocalDate today = LocalDate.now();
        LocalDate birthDate = user.getBirthDate();

        // 생일이 오늘인 경우
        return birthDate.getMonth() == today.getMonth() && birthDate.getDayOfMonth() == today.getDayOfMonth();
    }

    @Override
    @Transactional
    public void issue(User user) {

        // 중복 발급 방지: 오늘 기준으로 발급이 되었는지 확인
        boolean alreadyIssued = userCouponRepository.existsByUserAndCoupon_Name(user, COUPON_NAME);
        if(alreadyIssued) {
            return;
        }

        Coupon birthdayCoupon = couponRepository.findByName(COUPON_NAME)
                .orElseThrow(()-> new EcomosException(ErrorCode._NOT_FOUND_COUPON));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiredAt = now.plusDays(30); // 예: 30일 내 사용 가능

        UserCoupon userCoupon = new UserCoupon(user, birthdayCoupon, now, expiredAt);
        userCouponRepository.save(userCoupon);



    }

}
