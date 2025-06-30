package org.personal.comerspleject.domain.point.repository;

import org.personal.comerspleject.domain.point.entitty.PointHistory;
import org.personal.comerspleject.domain.point.entitty.PointType;
import org.personal.comerspleject.domain.users.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

    List<PointHistory> findByUserOrderCreatedAtDesc(User user);

    // 유저 기준, 최신순, 페이지 정리
    Page<PointHistory> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    // 포인트 사용
    List<PointHistory> findByUserTypeAndExpiresAtAfter(User user, PointType pointType, LocalDateTime now);

    int sumByUserAndType(@Param("user") User user, @Param("type") PointType type);
}
