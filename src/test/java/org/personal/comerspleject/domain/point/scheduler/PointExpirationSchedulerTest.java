package org.personal.comerspleject.domain.point.scheduler;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.personal.comerspleject.domain.point.entitty.Point;
import org.personal.comerspleject.domain.point.entitty.PointHistory;
import org.personal.comerspleject.domain.point.entitty.PointType;
import org.personal.comerspleject.domain.point.repository.PointHistoryRepository;
import org.personal.comerspleject.domain.point.repository.PointRepository;
import org.personal.comerspleject.domain.users.user.entity.User;
import org.personal.comerspleject.domain.users.user.entity.UserRole;
import org.personal.comerspleject.domain.users.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PointExpirationSchedulerTest {

    @Autowired private PointExpirationScheduler scheduler;
    @Autowired private PointRepository pointRepository;
    @Autowired private PointHistoryRepository pointHistoryRepository;
    @Autowired private UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void clean() {
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE point_history").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE point").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE cart").executeUpdate(); // 참조 테이블도 삭제
        entityManager.createNativeQuery("TRUNCATE TABLE users").executeUpdate();
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
    }

    @Test
    void 포인트가_만료되면_정상적으로_차감되고_이력에_기록된다() {
        // given
        User user = createTestUser(); // 매번 다른 user_uid 보장
        userRepository.save(user);

        Point point = new Point(user, 2000);
        pointRepository.save(point);

        LocalDateTime now = LocalDateTime.now();

        // createdAt: 10분 전, 나노초 1000 / expiredAt: 어제 → 만료 대상
        PointHistory expiredEarn = createPointHistory(user, 1000, PointType.EARNED,
                now.minusMinutes(10).withSecond(10).withNano(0),
                now.minusDays(1));
        pointHistoryRepository.save(expiredEarn);

        // createdAt: 5분 전, 나노초 2000 / expiredAt: 없음 → 아직 유효
        PointHistory validEarn = createPointHistory(user, 1000, PointType.EARNED,
                now.minusMinutes(10).withSecond(20).withNano(0),
                null);
        pointHistoryRepository.save(validEarn);

        // when
        scheduler.expirePoints();

        // then
        Point updatedPoint = pointRepository.findByUser(user).orElseThrow();
        assertEquals(1000, updatedPoint.getTotalPoint());

        List<PointHistory> histories = pointHistoryRepository.findByUserOrderByCreatedAtDesc(user);
        assertTrue(histories.stream()
                .anyMatch(h -> h.getType() == PointType.EXPIRED && h.getAmount() == -1000));

        assertTrue(expiredEarn.isExpired());
        assertFalse(validEarn.isExpired());
    }

    // ✅ 매번 고유한 유저 생성 (user_uid 충돌 방지)
    private User createTestUser() {
        String uniqueEmail = "test_" + UUID.randomUUID() + "@example.com";
        return new User(uniqueEmail, "pw", "테스트", UserRole.USER, "서울시", LocalDate.of(1995, 1, 1));
    }

    // ✅ createdAt, expiredAt 명시적으로 설정
    private PointHistory createPointHistory(User user, int amount, PointType type,
                                            LocalDateTime createdAt, LocalDateTime expiredAt) {
        PointHistory history = new PointHistory(user, amount, type);
        if (createdAt != null) {
            history.setCreatedAt(createdAt);
        }
        if (expiredAt != null) {
            history.setExpiredAt(expiredAt);
        }
        return history;
    }
}

/**
 * 테스트의 목적 - 사용자의 적립 포인트 중 만료 대상인 포인트를 자동으로 차감, 차감 이력을 EXPIRED타입으로 기록
 *
 *테스트의 시나리오
 * 1.
 * 적립된 포인트 2건을 생성
 * 하나는 만료된 포인트 (expiredAt: 어제)
 * 하나는 아직 유효한 포인트 (expiredAt: null)
 * 2.
 * expirePoints() 실행
 * 만료된 포인트는 자동으로 차감되고
 * 새로운 PointHistory가 생성됨 (type: EXPIRED, amount: -1000)
 *
 * 3. 검증
 * Point.totalPoint가 1000으로 줄어들었는지
 * PointHistory에 EXPIRED 타입이 존재하는지
 * 만료 플래그(expired)가 정확히 반영되었는지
 *
 * 문제점
 * 1. 유니크 제약 충돌(Duplicate entry)
 * PointHistory 테이블에 (user_uid, created_at) 조합에 유니크 제약이 설정되어 있었음
 * 테스트 수행 시, LocalDateTime.now() 값이 거의 동시에 발생하며 createdAt이 중복 → 충돌 발생
 *
 * 2. TRUNCATE 에러
 * 테스트 @BeforeEach에서 테이블 초기화를 위해 TRUNCATE 사용
 * 하지만 외래 키 제약 조건(FOREIGN KEY)이 있는 상태에선 TRUNCATE 불가능 → 실행 시 실패
 */