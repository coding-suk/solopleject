package org.personal.comerspleject.domain.user.dto;

import lombok.Getter;

@Getter
public class UserResponseDto {

    private Long userId;

    public UserResponseDto(Long userId) {
        this.userId = userId;
    }

}
