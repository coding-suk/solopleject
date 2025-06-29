package org.personal.comerspleject.domain.point.controller;

import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.domain.point.dto.response.PointHistoryResponseDto;
import org.personal.comerspleject.domain.point.entitty.PointHistory;
import org.personal.comerspleject.domain.point.service.PointService;
import org.personal.comerspleject.domain.users.user.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ecomos/mypage/points")
public class PointController {

    private final PointService pointService;

    // 사용 내역 조회
    @GetMapping
    public List<PointHistoryResponseDto> getPointHistory(@AuthenticationPrincipal User user) {
        return pointService.getPointHistory(user);
    }

}
