package org.personal.comerspleject.domain.users.user.dto.response;

import lombok.Getter;
import org.personal.comerspleject.domain.order.dto.response.RecentOrderResponseDto;
import org.personal.comerspleject.domain.users.user.entity.UserRole;

import java.time.LocalDateTime;

@Getter
public class UserInfoResponseDto {

    private Long userId;

    private String name;

    private String email;

    private String address;

    private UserRole role;

    private int point;

    private LocalDateTime createdAt;

    private int couponCount;

    private LocalDateTime recentOrderDate;

    private RecentOrderResponseDto recentOrder;

    public UserInfoResponseDto(Long userId, String name, String email,
                               String address, UserRole role,
                               int point, LocalDateTime createdAt,
                               int couponCount,
                               LocalDateTime recentOrderDate,
                               RecentOrderResponseDto recentOrder) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.address = address;
        this.role = role;
        this.point = point;
        this.createdAt = createdAt;
        this.couponCount = couponCount;
        this.recentOrderDate = recentOrderDate;
        this.recentOrder = recentOrder;

    }

}
