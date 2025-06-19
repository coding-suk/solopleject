package org.personal.comerspleject.domain.order.controller;

import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.domain.order.dto.request.OrderRequestDto;
import org.personal.comerspleject.domain.order.dto.response.OrderResponseDto;
import org.personal.comerspleject.domain.order.entity.OrderStatus;
import org.personal.comerspleject.domain.order.service.OrderServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ecomos/order")
public class OrderController {

    private final OrderServiceImpl orderService;

    // 주문 생성
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        OrderResponseDto orderResponseDto = orderService.createOrder(orderRequestDto.getUserId(), orderRequestDto);
        return ResponseEntity.ok(orderResponseDto);
    }

    // 주문 단건 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    // 주문 전체 조회 (특정 유저의)
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getOrdersByUser(@RequestParam Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUser(userId));
    }

    // 주문 상태 변경
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(@PathVariable Long orderId,
                                                              @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
    }

    // 주문 취소
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }

}
