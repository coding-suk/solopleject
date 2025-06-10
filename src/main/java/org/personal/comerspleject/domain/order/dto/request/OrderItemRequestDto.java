package org.personal.comerspleject.domain.order.dto.request;

import lombok.Getter;

@Getter
public class OrderItemRequestDto {

    //클라이언트가 보낸 주문 상품 개별 정보
    private Long productId;

    private int quantity;

    public OrderItemRequestDto(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

}
