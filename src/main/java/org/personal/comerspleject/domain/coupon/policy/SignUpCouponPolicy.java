package org.personal.comerspleject.domain.coupon.policy;

import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.config.exception.EcomosException;
import org.personal.comerspleject.config.exception.ErrorCode;
import org.personal.comerspleject.domain.coupon.dto.CouponResponseDto;
import org.personal.comerspleject.domain.coupon.entity.Coupon;
import org.personal.comerspleject.domain.coupon.entity.UserCoupon;
import org.personal.comerspleject.domain.coupon.repository.CouponRepository;
import org.personal.comerspleject.domain.coupon.repository.UserCouponRepository;
import org.personal.comerspleject.domain.users.user.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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
        Coupon coupon = couponRepository.findByName(COUPON_NAME)
                .orElseThrow(() -> new EcomosException(ErrorCode._NOT_FOUND_COUPON));

        userCouponRepository.save(new UserCoupon(user, coupon, LocalDateTime.now(), coupon.getExpiredAt()));
    }

}
