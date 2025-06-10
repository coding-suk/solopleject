package org.personal.comerspleject.domain.order.service;

import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.domain.order.dto.request.OrderRequestDto;
import org.personal.comerspleject.domain.order.dto.response.OrderResponseDto;
import org.personal.comerspleject.domain.order.repository.OrderRepository;
import org.personal.comerspleject.domain.users.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public interface OrderService {

    OrderResponseDto createOrder(User user, OrderRequestDto requestDto);

}
