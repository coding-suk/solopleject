package org.personal.comerspleject.domain.policyAndscheduler;

import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.domain.users.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CouponPolicyRunner {

    private final List<CouponPolicy> couponPolicies;

    public void run(User user) {
        for(CouponPolicy policy : couponPolicies) {
            if(policy.supports(user)) {
                policy.issue(user);
            }
        }
    }

}
