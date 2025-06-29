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

    @OneToOne
    private User user;

    private int amount;

    @Enumerated(EnumType.STRING)
    private PointType type;

    private LocalDateTime createdAt;

    public PointHistory(User user, int amount, PointType type) {
        this.user = user;
        this.amount = amount;
        this.type = type;
        this.createdAt = LocalDateTime.now();
    }

}
