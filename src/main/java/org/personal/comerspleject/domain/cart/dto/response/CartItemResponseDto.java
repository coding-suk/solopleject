package org.personal.comerspleject.domain.cart.dto.response;

import lombok.Getter;

@Getter
public class CartItemResponseDto {

    // 장바구니를 전체 조회할때

    private Long productId;

    private String productName;

    private int quantity;

    private Integer price;

    public CartItemResponseDto(String productName, int quantity, Integer price) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

}
