package org.personal.comerspleject.domain.point.service;

import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.config.exception.EcomosException;
import org.personal.comerspleject.config.exception.ErrorCode;
import org.personal.comerspleject.domain.point.entitty.Point;
import org.personal.comerspleject.domain.point.entitty.PointHistory;
import org.personal.comerspleject.domain.point.entitty.PointType;
import org.personal.comerspleject.domain.point.repository.PointHistoryRepository;
import org.personal.comerspleject.domain.point.repository.PointRepository;
import org.personal.comerspleject.domain.users.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointService {

    private final PointRepository pointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    @Transactional
    public void earnPoint(User user, int amount) {

        Point point = pointRepository.findByUser(user)
                .orElseGet(() -> pointRepository.save(new Point(user, 0)));

        point.addPoint(amount);

        pointHistoryRepository.save(new PointHistory(user, amount, PointType.EARNED));

    }

    @Transactional
    public void usePoint(User user, int amount) {

        Point point = pointRepository.findByUser(user)
                .orElseThrow(() -> new EcomosException(ErrorCode._NOT_FOUND_POINT));

        point.usePoint(amount);

        pointHistoryRepository.save(new PointHistory(user, amount * -1, PointType.USED));

    }

}
