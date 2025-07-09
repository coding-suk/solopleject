package org.personal.comerspleject.domain.coupon.repository;

import org.personal.comerspleject.domain.coupon.entity.Coupon;
import org.personal.comerspleject.domain.coupon.entity.CouponType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CouponRepository  extends JpaRepository<Coupon, Long> {

    // 쿠폰 타입으로 조회
    List<Coupon> findByCouponType(CouponType type);

    // 만료되지 않은 쿠폰만
    List<Coupon> findByExpiredAtAfter(LocalDateTime now);

    Optional<Coupon> findByName(String couponName);
}
