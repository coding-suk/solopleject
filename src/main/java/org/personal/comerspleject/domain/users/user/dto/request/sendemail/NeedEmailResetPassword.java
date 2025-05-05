package org.personal.comerspleject.domain.users.user.dto.request.sendemail;

import lombok.Getter;

@Getter
public class NeedEmailResetPassword {

    private String token;

    private String newPassword1;

}
