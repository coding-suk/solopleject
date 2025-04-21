package org.personal.comerspleject.common.dto.response;

import lombok.Getter;

@Getter
public class CommonResponseDto {

    private String message;

    public CommonResponseDto(String message) {
        this.message = message;
    }

}
