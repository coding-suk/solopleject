package org.personal.comerspleject.domain.order.dto.response;

import lombok.Getter;
import org.personal.comerspleject.domain.order.entity.OrderItem;

@Getter
public class OrderItemResponseDto {

    private String productName;

    private int price;

    private int quantity;

    public OrderItemResponseDto(String productName, int price, int quantity) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public static OrderItemResponseDto from(OrderItem item) {
        return new OrderItemResponseDto(
                item.getProduct().getName(),
                item.getPrice(),
                item.getQuantity()
        );
    }


}
