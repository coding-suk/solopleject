package org.personal.comerspleject.domain.user.dto.response;

import lombok.Getter;

@Getter
public class UpdateUserResponseDto {

    private String email;

    private String address;

    public UpdateUserResponseDto(String email, String address) {
        this.email = email;
        this.address = address;
    }
}
