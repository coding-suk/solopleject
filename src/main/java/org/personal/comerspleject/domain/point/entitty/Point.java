package org.personal.comerspleject.domain.point.entitty;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.personal.comerspleject.config.exception.EcomosException;
import org.personal.comerspleject.config.exception.ErrorCode;
import org.personal.comerspleject.domain.users.user.entity.User;

@Entity
@Getter
@NoArgsConstructor
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pId;

    @OneToOne
    private User user;

    private int totalPoint;

    public Point(User user, int totalPoint) {
        this.user = user;
        this.totalPoint = totalPoint;
    }

    // 포인트 획득
    public void addPoint(int amount) {
        this.totalPoint += amount;
    }

    // 포인트 차감
    public void usePoint(int amount) {
        if(this.totalPoint < amount) {
            throw new EcomosException(ErrorCode._NOT_ENOUGH_POINT);
        }
        this.totalPoint -= amount;
    }

    public void subtractPoint(int amount) {
        this.totalPoint = Math.max(0, this.totalPoint - amount);
    }

}
