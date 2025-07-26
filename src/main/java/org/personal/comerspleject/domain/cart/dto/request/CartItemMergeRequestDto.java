package org.personal.comerspleject.domain.cart.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemMergeRequestDto {

    private Long productId;

    private int quantity;

    public CartItemMergeRequestDto(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }


}
