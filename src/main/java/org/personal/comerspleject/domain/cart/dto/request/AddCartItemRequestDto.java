package org.personal.comerspleject.domain.cart.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCartItemRequestDto {

    // 물품 추가할때 사용

    private Long productId;
    private int quantity;

}

