package org.personal.comerspleject.domain.coupon.controller;

import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.domain.coupon.dto.CouponResponseDto;
import org.personal.comerspleject.domain.coupon.entity.Coupon;
import org.personal.comerspleject.domain.coupon.service.CouponService;
import org.personal.comerspleject.domain.coupon.service.UserCouponService;
import org.personal.comerspleject.domain.users.user.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ecomos/coupons")
public class CouponController {

    private final CouponService couponService;

    // 전체 쿠폰 목록
    @GetMapping
    public ResponseEntity<List<CouponResponseDto>> getAllCoupons() {
        return ResponseEntity.ok(couponService.getAllCoupons());
    }

    // 쿠폰 상세 조회
    @GetMapping("/{couponId}")
    public ResponseEntity<CouponResponseDto> getCouponDetail(@PathVariable Long couponId) {
        return ResponseEntity.ok(couponService.getCouponDetail(couponId));
    }

    // 쿠폰 발급
    @PostMapping("/{couponId}/issue")
    public ResponseEntity<Void> issueCoupon(@AuthenticationPrincipal User user,
                                            @PathVariable Long couponId) {
        couponService.issueCouponToUser(user, couponId);
        return ResponseEntity.ok().build();
    }


}
