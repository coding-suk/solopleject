package org.personal.comerspleject.domain.coupon.controller;

import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.domain.coupon.entity.UserCoupon;
import org.personal.comerspleject.domain.coupon.service.UserCouponService;
import org.personal.comerspleject.domain.users.user.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ecomos/user-coupon")
public class UserCouponController {

    private final UserCouponService userCouponService;

    // 로그인 유저 보유 쿠폰 조회
    @GetMapping
    public ResponseEntity<List<UserCoupon>> getMyCoupons(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userCouponService.getAvailableCoupons(user));
    }

    // 유저 쿠폰 사용 처리
    @PostMapping("/{userCouponId}/use")
    public ResponseEntity<Void> useCoupon(@PathVariable Long userCouponId) {
        userCouponService.useCoupon(userCouponId);
        return ResponseEntity.ok().build();
    }

}
