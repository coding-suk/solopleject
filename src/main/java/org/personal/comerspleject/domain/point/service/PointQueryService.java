package org.personal.comerspleject.domain.point.service;

import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.domain.point.dto.response.ExpiringPointResponseDto;
import org.personal.comerspleject.domain.point.dto.response.PointHistoryResponseDto;
import org.personal.comerspleject.domain.point.dto.response.PointSummaryResponseDto;
import org.personal.comerspleject.domain.point.entitty.Point;
import org.personal.comerspleject.domain.point.entitty.PointHistory;
import org.personal.comerspleject.domain.point.entitty.PointType;
import org.personal.comerspleject.domain.point.repository.PointHistoryRepository;
import org.personal.comerspleject.domain.point.repository.PointRepository;
import org.personal.comerspleject.domain.users.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PointQueryService {

    private final PointHistoryRepository pointHistoryRepository;
    private final PointRepository pointRepository;

    // 페이지
    public Page<PointHistoryResponseDto> getPointHistory(User user, Pageable pageable) {
        return pointHistoryRepository.findByUser(user, pageable).
                map(PointHistoryResponseDto::new);
    }

    public PointSummaryResponseDto getPointSummary(User user) {
        int totalEarned = pointHistoryRepository.sumByUserAndType(user, PointType.EARNED);
        int totalUsed = pointHistoryRepository.sumByUserAndType(user, PointType.USED);
        int totalPoint = pointRepository.findByUser(user)
                .map(Point::getTotalPoint)
                .orElse(0);
        return new PointSummaryResponseDto(totalPoint, totalEarned, totalUsed);

    }
    // --
    // 만료 포인트 조회
    public ExpiringPointResponseDto getExpiringPoint(User user, int withinDays) {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime to = now.plusDays(withinDays);

        List<PointHistory> expiring = pointHistoryRepository.findByExpiringSoon(
                user, PointType.EARNED, now, to
        );

        return new ExpiringPointResponseDto(expiring, to);

    }
}
