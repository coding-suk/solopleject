package org.personal.comerspleject.domain.point.scheduler;

import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.config.exception.EcomosException;
import org.personal.comerspleject.config.exception.ErrorCode;
import org.personal.comerspleject.domain.point.entitty.Point;
import org.personal.comerspleject.domain.point.entitty.PointHistory;
import org.personal.comerspleject.domain.point.entitty.PointType;
import org.personal.comerspleject.domain.point.repository.PointHistoryRepository;
import org.personal.comerspleject.domain.point.repository.PointRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PointExpirationScheduler {

    private final PointRepository pointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional
    public void expirePoints() {

        List<PointHistory> expiredHistories = pointHistoryRepository
                .findByTypeAndExpiresAtBefore(PointType.EARNED, LocalDateTime.now());

        for (PointHistory history : expiredHistories) {
            if(history.isExpired()) continue; // 이미 처리된 내용이면 넘김

            Point point = pointRepository.findByUser(history.getUser())
                    .orElseThrow(() -> new EcomosException(ErrorCode._NOT_FOUND_POINT));

            int expiredAmount = history.getAmount();

            // 현제 totalpoint보다 클 경우 차감하지 않고 0으로 설정
            point.subtractPoint(Math.min(expiredAmount, point.getTotalPoint()));

            pointHistoryRepository.save(new PointHistory(
                    history.getUser(),
                    -expiredAmount,
                    PointType.EXPIRED
            ));

            history.markAsExpired();

        }
    }
}
