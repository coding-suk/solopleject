package org.personal.comerspleject.domain.cart.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class CartResponseDto {

    // 본인 장바구니를 전체 조회할떄

    private Long cartId;

    private List<CartItemResponseDto> items;

    private Integer totalPrice;

    public CartResponseDto(List<CartItemResponseDto> items, Integer totalPrice) {
        this.items = items;
        this.totalPrice = totalPrice;
    }

}
