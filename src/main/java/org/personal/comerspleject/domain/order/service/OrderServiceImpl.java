package org.personal.comerspleject.domain.order.service;

import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.config.exception.EcomosException;
import org.personal.comerspleject.config.exception.ErrorCode;
import org.personal.comerspleject.domain.order.dto.request.OrderRequestDto;
import org.personal.comerspleject.domain.order.dto.response.OrderResponseDto;
import org.personal.comerspleject.domain.order.entity.Order;
import org.personal.comerspleject.domain.order.entity.OrderItem;
import org.personal.comerspleject.domain.order.entity.OrderStatus;
import org.personal.comerspleject.domain.order.repository.OrderRepository;
import org.personal.comerspleject.domain.users.seller.entity.Product;
import org.personal.comerspleject.domain.users.seller.repository.ProductRepository;
import org.personal.comerspleject.domain.users.user.entity.User;
import org.personal.comerspleject.domain.users.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService{

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public OrderResponseDto createOrder(Long userId, OrderRequestDto orderRequestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new EcomosException(ErrorCode._NOT_FOUND_USER));

        // 새 주문 객체 생성
        Order order = new Order(user, OrderStatus.WAITING_FOR_DELIVERY);

        // 요청된 상품들을 기반으로 orderItem 생성
        orderRequestDto.getItems().forEach(orderItemRequestDto -> {
            Product product = productRepository.findById(orderItemRequestDto.getProductId())
                    .orElseThrow(() -> new EcomosException(ErrorCode._NOT_FOUND_PRODUCT));

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(orderItemRequestDto.getQuantity());
            orderItem.setPrice(product.getPrice());

            order.addOrderItem(orderItem);
        });

        // 총 가격 계산
        order.calculateTotalPrice();

        // 저장
        orderRepository.save(order);

        return OrderResponseDto.from(order);
    }

    @Override
    public OrderResponseDto getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EcomosException(ErrorCode._NOT_FOUND_ORDER));
        return OrderResponseDto.from(order);
    }

    @Override
    public List<OrderResponseDto> getOrdersByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EcomosException(ErrorCode._NOT_FOUND_USER));

        List<Order> orders = orderRepository.findAllByUser(user);
        return orders.stream()
                .map(OrderResponseDto::from)
                .toList();
    }

    @Override
    public OrderResponseDto updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EcomosException(ErrorCode._NOT_FOUND_ORDER));

        order.setStatus(status);
        return OrderResponseDto.from(order);
    }

    @Override
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EcomosException(ErrorCode._NOT_FOUND_ORDER));

        orderRepository.delete(order);
    }

}
