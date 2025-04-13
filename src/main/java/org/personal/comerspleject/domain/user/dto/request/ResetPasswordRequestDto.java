package org.personal.comerspleject.domain.user.dto.request;

import lombok.Getter;

@Getter
public class ResetPasswordRequestDto {

    private String email;
    private String newPassword;

}
