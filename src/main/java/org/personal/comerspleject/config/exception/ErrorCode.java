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

    _NOT_PERMITTED_USER(HttpStatus.BAD_REQUEST, "허용되지 않는 사용자 입니다"),

    _BAD_REQUEST_INVALID_EMAIL(HttpStatus.BAD_REQUEST, "유효하지 않는 이메일입니다"),
    _DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "중복된 이메일입니다"),
    _INVALID_PASSWORD_FORM(HttpStatus.BAD_REQUEST, "유효하지 않는 비밀번호 형식입니다"),

    _INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),

    _OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "재고가 부족합니다"),
    _NOT_FOUND_PRODUCT(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다"),
    _INVALID_USER_ROLE(HttpStatus.UNAUTHORIZED, "유효하지 않는 유저 role입니다"),


    _FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    _EMPTY_CART(HttpStatus.NOT_FOUND, "장바구니가 없습니다"),
    _NOT_FOUND_PRODUCT_IN_CART(HttpStatus.NOT_FOUND, "장바구니에 해당 상품이 없습니다");




    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
