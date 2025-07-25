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

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SignUpCouponPolicy  implements CouponPolicy{

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    private static final String COUPON_NAME = "회원가입 축하 쿠폰";

    @Override
    public boolean supports (User user) {
        // 가입일 기준 하루 이내, 아직 쿠폰 안 받을 경우
        return user.getCreatedAt().isAfter(LocalDateTime.now().minusDays(1))
                && !userCouponRepository.existsByUserAndCoupon_Name(user, COUPON_NAME);
    }

    @Override
    public void issue(User user) {
        List<Coupon> signupCoupons = couponRepository.findAllByName(COUPON_NAME);

        if (signupCoupons.isEmpty()) {
            throw new EcomosException(ErrorCode._NOT_FOUND_COUPON);
        }

        // 최신 쿠폰만 남기고 나머지 중복 쿠폰 삭제
        signupCoupons.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt())); // 최신순 정렬
        Coupon latestCoupon = signupCoupons.get(0); // 가장 최신 쿠폰

        if (signupCoupons.size() > 1) {
            List<Coupon> duplicates = signupCoupons.subList(1, signupCoupons.size());
            couponRepository.deleteAll(duplicates);
        }

        // 사용자에게 쿠폰 발급
        userCouponRepository.save(new UserCoupon(user, latestCoupon, LocalDateTime.now(), latestCoupon.getExpiredAt()));
    }

}
