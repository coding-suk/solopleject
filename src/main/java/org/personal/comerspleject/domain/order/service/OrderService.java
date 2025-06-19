package org.personal.comerspleject.domain.order.service;

import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.domain.order.dto.request.OrderRequestDto;
import org.personal.comerspleject.domain.order.dto.response.OrderResponseDto;
import org.personal.comerspleject.domain.order.entity.OrderStatus;
import org.personal.comerspleject.domain.order.repository.OrderRepository;
import org.personal.comerspleject.domain.users.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderService {

    OrderResponseDto createOrder(Long user, OrderRequestDto requestDto);

    OrderResponseDto getOrder(Long orderId);

    List<OrderResponseDto> getOrdersByUser(Long userId);

    OrderResponseDto updateOrderStatus(Long orderId, OrderStatus status);

    void cancelOrder(Long orderId);

}
