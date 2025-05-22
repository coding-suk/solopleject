package org.personal.comerspleject.domain.cart.dto.request;

import lombok.Getter;

@Getter
public class AddCartItemRequestDto {

    // 물품 추가할때 사용

    private Long productId;
    private int quantity;

}

