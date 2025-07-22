package org.personal.comerspleject.domain.point.entitty;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.personal.comerspleject.domain.users.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
/*
DB에서 명시를 해줬지만, 혼돈을 막기 위해 표기
@Table(
        name = "point_history",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_user_created_type", // 명시적으로 이름 부여
                columnNames = {"user_uid", "created_at", "type"}
        )
)
 */
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

    // 테스트 용
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
