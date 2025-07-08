package org.personal.comerspleject.domain.coupon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.personal.comerspleject.domain.users.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ucId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_cId")
    private Coupon coupon;

    private boolean used = false;
    private boolean expired = false;

    private LocalDateTime issuedAt;
    private LocalDateTime expiredAt; // 사용자 기준 만료일(null 허용)

    public UserCoupon(User user, Coupon coupon, LocalDateTime issuedAt, LocalDateTime expiredAt) {
        this.user = user;
        this.coupon = coupon;
        this.issuedAt = issuedAt;
        this.expiredAt = expiredAt;
    }

    public void markAsUsed() {
        this.used = true;
    }

    public void markAsExpired() {
        this.expired = true;
    }
}
