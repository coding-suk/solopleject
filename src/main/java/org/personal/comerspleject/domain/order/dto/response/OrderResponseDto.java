package org.personal.comerspleject.domain.order.dto.response;

import lombok.Getter;
import org.personal.comerspleject.domain.order.entity.Order;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderResponseDto {

    private Long orderId;

    private int totalPrice;

    private String status;

    private List<OrderItemResponseDto> items;

    public OrderResponseDto(Long orderId, int totalPrice, String status, List<OrderItemResponseDto> items) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.status = status;
        this.items = items;
    }

    public static OrderResponseDto from(Order order) {
        return new OrderResponseDto(
                order.getOId(),
                order.getTotalPrice(),
                order.getStatus().name(),
                order.getOrderItems().stream()
                        .map(OrderItemResponseDto::from)
                        .collect(Collectors.toList())
        );
    }

}
