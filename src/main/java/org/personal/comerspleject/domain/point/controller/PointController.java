package org.personal.comerspleject.domain.point.controller;

import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.domain.point.dto.response.PointHistoryResponseDto;
import org.personal.comerspleject.domain.point.dto.response.PointSummaryResponseDto;
import org.personal.comerspleject.domain.point.entitty.PointHistory;
import org.personal.comerspleject.domain.point.service.PointQueryService;
import org.personal.comerspleject.domain.point.service.PointService;
import org.personal.comerspleject.domain.users.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    private final PointQueryService pointQueryService;

    // 사용 내역 조회
    @GetMapping
    public List<PointHistoryResponseDto> getPointHistory(@AuthenticationPrincipal User user) {
        return pointService.getPointHistory(user);
    }

    // 페이지는 10페이지 단위로 보임
    @GetMapping
    public Page<PointHistoryResponseDto> getPointHistory(@AuthenticationPrincipal User user,
                                                         @PageableDefault(size = 10) Pageable pageable) {
        return pointQueryService.getPointHistory(user, pageable);
    }

    // 쿠폰 유효기간, 누적 통계
    @GetMapping("/summary")
    public PointSummaryResponseDto getSummary(@AuthenticationPrincipal User user) {
        return pointQueryService.getPointSummary(user);
    }


}
