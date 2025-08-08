package org.personal.comerspleject.config.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EcomosException.class)
    public ResponseEntity<ErrorResponse> handleEcomosException(EcomosException ex) {
        return ResponseEntity
                .status(ex.getErrorCode().getStatus())
                .body(new ErrorResponse(ex.getErrorCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        ex.printStackTrace(); // 로그로 남기기
        return ResponseEntity
                .status(500)
                .body(new ErrorResponse("예상치 못한 서버 오류가 발생했습니다.", 500));
    }
}
