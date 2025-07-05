package org.personal.comerspleject.domain.point.entitty;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.personal.comerspleject.domain.users.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class PointHistory {

    // 적립, 사용 이력
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long phId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uid")
    private User user;

    private int amount;

    @Enumerated(EnumType.STRING)
    private PointType type;

    private LocalDateTime createdAt;

    private LocalDateTime expiredAt; // 만료일

    private boolean expired = false; // 이미 소멸 처리된 내역인지 확인용

    public PointHistory(User user, int amount, PointType type) {
        this.user = user;
        this.amount = amount;
        this.type = type;
        this.createdAt = LocalDateTime.now();

        if(type == PointType.EARNED) {
            this.expiredAt = this.createdAt.plusMonths(6); // 반년 유효
        }
    }
    public void markAsExpired() {
        this.expired = true;
    }

    // 테스트 용
    public void setExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

}
