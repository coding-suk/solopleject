package org.personal.comerspleject.domain.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AdminUpdateRequestDto {

    @NotBlank
    private String name;

    private String address;

}
