package org.personal.comerspleject.domain.users.user.dto.request;

import lombok.Getter;

@Getter
public class ResetPasswordRequestDto {

    private String email;
    private String newPassword;

    public ResetPasswordRequestDto(String email, String newPassword) {
        this.email = email;
        this.newPassword = newPassword;
    }

}
