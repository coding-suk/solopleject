package org.personal.comerspleject.domain.point.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.personal.comerspleject.domain.point.entitty.PointHistory;
import org.personal.comerspleject.domain.point.entitty.PointType;

import java.time.LocalDateTime;

public class PointHistoryResponseDto {

    private int amount;
    private PointType type;
    private LocalDateTime createdAt;

    public PointHistoryResponseDto(PointHistory history) {
        this.amount = history.getAmount();
        this.type = history.getType();
        this.createdAt = history.getCreatedAt();
    }

}
