package org.personal.comerspleject.domain.cart.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddCartItemRequestDto {

    // 물품 추가할때 사용
    private Long productId;
    private int quantity;

    public CartItemDto toCartItem() {
        return new CartItemDto(this.productId, this.quantity);
    }

}

