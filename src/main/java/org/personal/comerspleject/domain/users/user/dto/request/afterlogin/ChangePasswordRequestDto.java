package org.personal.comerspleject.domain.users.user.dto.request.afterlogin;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChangePasswordRequestDto {

    private String currentPassword;

    private String newPassword;

}
