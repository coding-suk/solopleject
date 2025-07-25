package org.personal.comerspleject.common.schedule;

import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.domain.coupon.policyAndscheduler.CouponPolicyRunner;
import org.personal.comerspleject.domain.users.user.entity.User;
import org.personal.comerspleject.domain.users.user.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BirthdayCouponScheduler {

    private final UserRepository userRepository;
    private final CouponPolicyRunner couponPolicyRunner;

    // 매일 자정에 실행
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void issueBirthdayCoupons() {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            couponPolicyRunner.run(user); // 생일인 경우에만 내부적으로 발급
        }
    }

}
