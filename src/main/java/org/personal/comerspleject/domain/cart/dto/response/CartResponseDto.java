package org.personal.comerspleject.domain.cart.dto.response;

import lombok.Getter;
import org.personal.comerspleject.domain.cart.entity.Cart;

import java.util.List;

@Getter
public class CartResponseDto {

    // 본인 장바구니를 전체 조회할떄

    private Long cartId;

    private List<CartItemResponseDto> items;

    private Integer totalPrice;

    public CartResponseDto(Long cartId, List<CartItemResponseDto> items, Integer totalPrice) {
        this.cartId = cartId;
        this.items = items;
        this.totalPrice = totalPrice;
    }

    public static CartResponseDto from(Cart cart) {
        List<CartItemResponseDto> itemDtos = cart.getItems().stream()
                .map(CartItemResponseDto::from)
                .toList();

        int total = itemDtos.stream().mapToInt(i -> i.getPrice() * i.getQuantity()).sum();

        return new CartResponseDto(cart.getCId(), itemDtos, total);

    }

}
