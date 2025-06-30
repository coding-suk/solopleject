package org.personal.comerspleject.domain.point.service;

import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.domain.point.dto.response.PointHistoryResponseDto;
import org.personal.comerspleject.domain.point.dto.response.PointSummaryResponseDto;
import org.personal.comerspleject.domain.point.entitty.Point;
import org.personal.comerspleject.domain.point.entitty.PointType;
import org.personal.comerspleject.domain.point.repository.PointHistoryRepository;
import org.personal.comerspleject.domain.point.repository.PointRepository;
import org.personal.comerspleject.domain.users.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointQueryService {

    private final PointHistoryRepository pointHistoryRepository;
    private final PointRepository pointRepository;

    // 페이지
    public Page<PointHistoryResponseDto> getPointHistory(User user, Pageable pageable) {
        return pointHistoryRepository.findByUserOrderByCreatedAtDesc(user, pageable).
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
}
