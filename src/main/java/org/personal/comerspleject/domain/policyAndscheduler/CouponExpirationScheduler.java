package org.personal.comerspleject.domain.policyAndscheduler;

import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.domain.coupon.service.UserCouponService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponExpirationScheduler {

    private final UserCouponService userCOuponService;

    // 자정마다 만료 처리
    @Scheduled(cron = "0 0 0 * * *") // 매일 자정 실행
    public void runCouponExpiration() {
        userCOuponService.expireCoupon();
    }

    // 아침 9시에 만료 예정 쿠폰 알림
    @Scheduled(cron = "0 0 9 * * *")
    public void runCouponExpirationNotice() {
        userCOuponService.notifyCouponsExpiringTomorrow();
    }

}
