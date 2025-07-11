package org.personal.comerspleject.domain.coupon.repository;

import org.personal.comerspleject.domain.coupon.entity.UserCoupon;
import org.personal.comerspleject.domain.users.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    // 만료 처리용
    @Query("SELECT uc FROM UserCoupon uc WHERE uc.expiredAt < :now AND uc.expired = false")
    List<UserCoupon> findCouponsToExpire(@Param("now") LocalDateTime now);

    // 사용자 보유 쿠폰 조회(사용X, 만료X)
    List<UserCoupon> findByUserAndExpiredAtFalseAndUsedFalse(User user);

    boolean existsByUserAndCoupon_Name(User user, String couponName);

    // 알림 대상 조회
    @Query("SELECT uc FROM UserCoupon uc WHERE uc.expiredAt BETWEEN :start AND :end AND uc.expired = false AND uc.used = false")
    List<UserCoupon> findCouponsExpiringTomorrow(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
