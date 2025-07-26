package org.personal.comerspleject.domain.order.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.personal.comerspleject.config.exception.EcomosException;
import org.personal.comerspleject.config.exception.ErrorCode;
import org.personal.comerspleject.domain.users.user.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long oId;

    // 연관된 유저(다대일)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private int totalPrice;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<OrderItem> orderItems = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<OrderItem> orderItem = new ArrayList<>();

    @Column(updatable = false)
    private LocalDateTime createdAt;

    public Order(User user, OrderStatus status) {
        this.user = user;
        this.status = status;
        this.totalPrice = 0;
    }

    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
        item.setOrder(this);
    }

    // 총 가격 계산
    public void calculateTotalPrice() {
        this.totalPrice = orderItems.stream()
                .mapToInt(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void updateStatus(OrderStatus newStatus) {
        // 현재 상태 기준으로 유효한 전이만 허용 (선택사항)
        if (this.status == OrderStatus.WAITING_FOR_PAYMENT && newStatus == OrderStatus.WAITING_FOR_DELIVERY) {
            this.status = newStatus;
        } else if (this.status == OrderStatus.WAITING_FOR_DELIVERY && newStatus == OrderStatus.DELIVERY) {
            this.status = newStatus;
        } else if (this.status == OrderStatus.DELIVERY && newStatus == OrderStatus.DELIVERY_COMPLETED) {
            this.status = newStatus;
        } else {
            throw new EcomosException(ErrorCode._INVALID_ORDER_STATUS_TRANSITION);
        }
    }


}
