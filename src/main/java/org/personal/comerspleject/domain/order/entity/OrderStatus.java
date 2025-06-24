package org.personal.comerspleject.domain.order.entity;

public enum OrderStatus {

    WAITING_FOR_PAYMENT, // 결제 전
    WAITING_FOR_DELIVERY, // 결제 완료 후 배송대기, 결제 완료, 배송 준비중
    DELIVERY, // 배송중
    DELIVERY_COMPLETED; // 배송 완료

}
