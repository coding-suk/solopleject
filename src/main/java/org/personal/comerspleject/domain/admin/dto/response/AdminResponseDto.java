package org.personal.comerspleject.domain.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.personal.comerspleject.domain.user.entity.User;
import org.personal.comerspleject.domain.user.entity.UserRole;

@Getter
@AllArgsConstructor
public class AdminResponseDto {

    private Long uid;

    private String email;

    private String name;

    private UserRole role;

    private Boolean isDeleted;

    private String address;

    public static AdminResponseDto from(User user) {
        return new AdminResponseDto(
                user.getUid(),
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.getIsdeleted(),
                user.getAddress()
        );
    }

}
