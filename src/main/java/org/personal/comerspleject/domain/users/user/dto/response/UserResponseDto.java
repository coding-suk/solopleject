package org.personal.comerspleject.domain.users.user.dto.response;

import lombok.Getter;

@Getter
public class UserResponseDto {

    private Long userId;

    public UserResponseDto(Long userId) {
        this.userId = userId;
    }

}
