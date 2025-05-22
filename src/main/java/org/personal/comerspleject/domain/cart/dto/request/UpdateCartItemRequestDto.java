package org.personal.comerspleject.domain.cart.dto.request;

import lombok.Getter;

@Getter
public class UpdateCartItemRequestDto {

    // 상품 수량변경시

    private Long productId;

    private int quantity;

}
