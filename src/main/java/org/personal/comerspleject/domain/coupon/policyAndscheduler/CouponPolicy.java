package org.personal.comerspleject.domain.coupon.policyAndscheduler;

import org.personal.comerspleject.domain.users.user.entity.User;

public interface CouponPolicy {

    // 유저에게 해당이 되는지 체크
    boolean supports(User user);

    // 해당 정책에 따른 쿠폰 발급
    void issue(User user);

}
