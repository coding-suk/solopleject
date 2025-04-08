package org.personal.comerspleject.config.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    _NOT_FOUND_TOKEN(HttpStatus.NOT_FOUND,  "JWT 토큰이 필요합니다."),
    _UNAUTHORIZED_INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다"),
    _UNAUTHORIZED_EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다"),
    _BAD_REQUEST_UNSUPPORTED_TOKEN(HttpStatus.BAD_REQUEST, "지원되지 않는 JWT 토큰입니다"),

    _USER_ROLE_IS_NULL(HttpStatus.BAD_REQUEST, "유저 권한이 없습니다"),
    _NOT_FOUND_USER(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다"),
    _PASSWORD_NOT_MATCHES(HttpStatus.NOT_FOUND, "비밀번호가 맞지 않습니다"),
    _DELETED_USER(HttpStatus.NOT_FOUND, "탈퇴한 유저 입니다"),

    _NOT_PERMITTED_USER(HttpStatus.BAD_REQUEST, "허용되지 않는 사용자 입니다");


    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
