package org.personal.comerspleject.domain.coupon.entity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.personal.comerspleject.domain.coupon.service.UserCouponService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserCouponScheduler {

    private final UserCouponService userCouponService;

    // 매일 자정 실행
    @Scheduled(cron = "0 0 0 * * *")
    public void expireUserCoupon() {
        log.info("쿠폰 만료 스케줄러 시작");
        userCouponService.expireCoupon();
        log.info("쿠론 만료 스케줄러 완료");
    }

}
