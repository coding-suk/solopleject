package org.personal.comerspleject.domain.point.repository;

import org.personal.comerspleject.domain.point.entitty.PointHistory;
import org.personal.comerspleject.domain.point.entitty.PointType;
import org.personal.comerspleject.domain.users.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

    List<PointHistory> findByUserOrderByCreatedAtDesc(User user);

    // 유저 기준, 최신순, 페이지 정리
    Page<PointHistory> findByUser(User user, Pageable pageable);

    // 포인트 사용
    List<PointHistory> findByUserAndTypeAndExpiredAtAfter(User user, PointType pointType, LocalDateTime now);

    @Query("SELECT COALESCE(SUM(ph.amount), 0) FROM PointHistory ph WHERE ph.user = :user AND ph.type = :type")
    int sumByUserAndType(@Param("user") User user, @Param("type") PointType type);

    @Query("""
    SELECT ph FROM PointHistory ph
    WHERE ph.type = :type
      AND ph.expiredAt < :now
      AND ph.expired = false
""")
    List<PointHistory> findUnexpiredExpiredPoints(@Param("type") PointType type,
                                                  @Param("now") LocalDateTime now);

    // 쿠폰 만료
    @Query("""
    SELECT ph FROM PointHistory ph 
    WHERE ph.user = :user 
      AND ph.type = :type 
      AND ph.expired = false 
      AND ph.expiredAt BETWEEN :from AND :to
      """)
    List<PointHistory> findByExpiringSoon(@Param("user") User user,
                                          @Param("type") PointType type,
                                          @Param("from") LocalDateTime from,
                                          @Param("to") LocalDateTime to);
}
