package org.personal.comerspleject.domain.cart.dto.response;

import lombok.Getter;
import org.personal.comerspleject.domain.cart.entity.CartItem;

@Getter
public class CartItemResponseDto {

    // 장바구니를 전체 조회할때

    private Long productId;

    private String productName;

    private int quantity;

    private Integer price;

    public CartItemResponseDto(Long productId, String productName, int quantity, Integer price) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    public static CartItemResponseDto from(CartItem item) {
        return new CartItemResponseDto(
                item.getProduct().getPId(),
                item.getProduct().getName(), // Product에 getName()이 있다고 가정
                item.getProduct().getPrice(), // Product에 getPrice()가 있다고 가정
                item.getQuantity()
        );
    }

}
