package org.personal.comerspleject.domain.payment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.personal.comerspleject.config.exception.EcomosException;
import org.personal.comerspleject.config.exception.ErrorCode;
import org.personal.comerspleject.domain.order.entity.Order;
import org.personal.comerspleject.domain.order.entity.OrderStatus;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pId;

    @OneToOne
    @JoinColumn(name = "order_oid")
    private Order order;

    private int amount;

    @Column(columnDefinition = "TEXT")
    private String snapshotJson;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private LocalDateTime paidAt;

    public Payment(Order order, int amount) {
        this.order = order;
        this.amount = amount;
        this.status = PaymentStatus.READY;
    }

    public void completePayment() {
        this.status = PaymentStatus.PAID;
        this.paidAt = LocalDateTime.now();
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public void completeWithOrder() {
        if(this.status != PaymentStatus.READY) {
            throw new EcomosException(ErrorCode._NOT_READY_TO_PAID);
        }

        this.status = PaymentStatus.PAID;
        this.paidAt = LocalDateTime.now();

        if(this.order.getStatus() == OrderStatus.WAITING_FOR_PAYMENT) {
            this.order.updateStatus(OrderStatus.WAITING_FOR_DELIVERY);
        } else {
            throw new EcomosException(ErrorCode._NOT_PAYADBLE_TO_ORDER_STATUS);
        }
    }

}
