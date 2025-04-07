package org.personal.comerspleject.config.exception;

import lombok.Getter;

@Getter
public class EcomosException extends RuntimeException{
    private ErrorCode errorCode;

    public EcomosException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public EcomosException(ErrorCode errorCode, String message) {
        super(errorCode.getMessage() + message);
        this.errorCode = errorCode;
    }
}
