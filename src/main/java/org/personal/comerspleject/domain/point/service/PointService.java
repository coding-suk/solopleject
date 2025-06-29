package org.personal.comerspleject.domain.point.service;

import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.config.exception.EcomosException;
import org.personal.comerspleject.config.exception.ErrorCode;
import org.personal.comerspleject.domain.point.dto.response.PointHistoryResponseDto;
import org.personal.comerspleject.domain.point.entitty.Point;
import org.personal.comerspleject.domain.point.entitty.PointHistory;
import org.personal.comerspleject.domain.point.entitty.PointType;
import org.personal.comerspleject.domain.point.repository.PointHistoryRepository;
import org.personal.comerspleject.domain.point.repository.PointRepository;
import org.personal.comerspleject.domain.users.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointService {

    private final PointRepository pointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    // 적립
    @Transactional
    public void earnPoint(User user, int amount) {

        Point point = pointRepository.findByUser(user)
                .orElseGet(() -> pointRepository.save(new Point(user, 0)));

        point.addPoint(amount);

        pointHistoryRepository.save(new PointHistory(user, amount, PointType.EARNED));

    }

    // 사용
    @Transactional
    public void usePoint(User user, int amount) {

        Point point = pointRepository.findByUser(user)
                .orElseThrow(() -> new EcomosException(ErrorCode._NOT_FOUND_POINT));

        point.usePoint(amount);

        pointHistoryRepository.save(new PointHistory(user, amount * -1, PointType.USED));

    }

    // 조회
    public List<PointHistoryResponseDto> getPointHistory(User user) {
        return pointHistoryRepository.findByUserOrderCreatedAtDesc(user).stream()
                .map(PointHistoryResponseDto::new)
                .collect(Collectors.toList());
    }

}
