package org.personal.comerspleject.domain.payment.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaymentSnapshot {

    private List<ItemSnapshot> items; // 주문 상품들
    private int itemTotalPrice; // 상품 총합
    private int discount; // 쿠폰
    private int shippingFee; // 배송비
    private int finalAmount; // 최종 결제금액

    @Getter
    @Setter
    public static class ItemSnapshot {
        private String productName;
        private int unitPrice;
        private int quantity;
    }
}
