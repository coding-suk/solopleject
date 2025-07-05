package org.personal.comerspleject.domain.point.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.personal.comerspleject.domain.point.entitty.PointHistory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ExpiringPointResponseDto {

    private int totalExpiring;

    private LocalDateTime expirationTo;

    private List<ExpiringDetail> details;

    @Getter
    @AllArgsConstructor
    public static class ExpiringDetail {
        private Long pointId;
        private int amount;
        private LocalDateTime expiredAt;
    }

    public ExpiringPointResponseDto(List<PointHistory> histories, LocalDateTime expirationTo) {
        this.totalExpiring = histories.stream().mapToInt(PointHistory::getAmount).sum();
        this.expirationTo = expirationTo;
        this.details = histories.stream()
                .map(p -> new ExpiringDetail(p.getPhId(), p.getAmount(), p.getExpiredAt()))
                .collect(Collectors.toList());
    }
}
