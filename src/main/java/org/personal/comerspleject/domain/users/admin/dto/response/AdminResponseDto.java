package org.personal.comerspleject.domain.users.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.personal.comerspleject.domain.users.user.entity.User;
import org.personal.comerspleject.domain.users.user.entity.UserRole;

@Getter
@AllArgsConstructor
public class AdminResponseDto {

    private Long uid;

    private String email;

    private String name;

    private UserRole role;

    private boolean isDeleted;

    private String address;

    public static AdminResponseDto from(User user) {
        return new AdminResponseDto(
                user.getUid(),
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.isDeleted(),
                user.getAddress()
        );
    }

}
