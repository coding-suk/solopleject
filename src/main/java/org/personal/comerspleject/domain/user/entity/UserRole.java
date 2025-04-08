package org.personal.comerspleject.domain.user.entity;

import org.personal.comerspleject.config.exception.EcomosException;
import org.personal.comerspleject.config.exception.ErrorCode;

import java.util.Arrays;

public enum UserRole {
    USER, ADMIN;

    public static UserRole of(String role) {
        return Arrays.stream(UserRole.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(()-> new EcomosException(ErrorCode._USER_ROLE_IS_NULL));
    }
}
