package org.personal.comerspleject.domain.point.scheduler;

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

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class PointExpirationSchedulerTest {

    @Autowired private PointExpirationScheduler scheduler;
    @Autowired private PointRepository pointRepository;
    @Autowired private PointHistoryRepository pointHistoryRepository;
    @Autowired private UserRepository userRepository;

    @Test
    void 포인트가_만료되면_정상적으로_차감되고_이력에_기록된다() {
        User user = new User("test@example.com", "password", "홍길동", UserRole.USER, "서울시");
        userRepository.save(user);

        Point point = new Point(user, 2000);
        pointRepository.save(point);

        PointHistory expiredEarn = new PointHistory(user, 1000, PointType.EARNED);
        expiredEarn.setExpiredAt(LocalDateTime.now().minusDays(1));
        pointHistoryRepository.save(expiredEarn);

        PointHistory validEarn = new PointHistory(user, 1000, PointType.EARNED);
        pointHistoryRepository.save(validEarn);

        scheduler.expirePoints();

        Point updatedPoint = pointRepository.findByUser(user).orElseThrow();
        assertEquals(1000, updatedPoint.getTotalPoint());

        List<PointHistory> histories = pointHistoryRepository.findByUserOrderByCreatedAtDesc(user);
        assertTrue(histories.stream()
                .anyMatch(h -> h.getType() == PointType.EXPIRED && h.getAmount() == -1000));

        assertTrue(expiredEarn.isExpired());
    }
}