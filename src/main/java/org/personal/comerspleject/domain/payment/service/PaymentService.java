package org.personal.comerspleject.domain.payment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.config.exception.EcomosException;
import org.personal.comerspleject.config.exception.ErrorCode;
import org.personal.comerspleject.domain.coupon.entity.Coupon;
import org.personal.comerspleject.domain.coupon.entity.UserCoupon;
import org.personal.comerspleject.domain.coupon.repository.UserCouponRepository;
import org.personal.comerspleject.domain.order.entity.Order;
import org.personal.comerspleject.domain.order.entity.OrderItem;
import org.personal.comerspleject.domain.order.entity.OrderStatus;
import org.personal.comerspleject.domain.order.repository.OrderRepository;
import org.personal.comerspleject.domain.payment.dto.CompletePaymentRequestDto;
import org.personal.comerspleject.domain.payment.dto.PaymentSnapshot;
import org.personal.comerspleject.domain.payment.entity.Payment;
import org.personal.comerspleject.domain.payment.repository.PaymentRepository;
import org.personal.comerspleject.domain.point.service.PointService;
import org.personal.comerspleject.domain.policyAndscheduler.CouponPolicyRunner;
import org.personal.comerspleject.domain.users.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final ObjectMapper objectMapper;
    private final PointService pointService;
    private final CouponPolicyRunner couponPolicyRunner;
    private final UserCouponRepository userCouponRepository;

    /*
    * 결제 준비
    * Order 상태가 WAITING_FOR_PAYMENT 인지 확인
    * 최신 가격 계산
    * Payment 생성 (status = READY)
    * snapshotJson 저장
    * */
    @Transactional
    public Long initiateMockPayment(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EcomosException(ErrorCode._NOT_FOUND_ORDER));

        if(order.getStatus() != OrderStatus.WAITING_FOR_PAYMENT) {
            throw new EcomosException(ErrorCode._NOT_AVAILABLE_FOR_PAYMENT);
        }

        order.calculateTotalPrice();

        PaymentSnapshot snapshot = createSnapshotFromOrder(order);

        String snapshotJson;
        try{
            snapshotJson = objectMapper.writeValueAsString(order.getOrderItems());
        } catch (JsonProcessingException e) {
            throw new EcomosException(ErrorCode._SNAP_SERIALIZATION_FAILED);
        }

        Payment payment = new Payment(order, order.getTotalPrice());
        payment.setSnapshotJson(snapshotJson);

        paymentRepository.save(payment);
        return payment.getPId();
    }

    /*
    * 결제 완료 처리
    * payment 상태를 paid로 변경
    * order 상태를 waitint_for_delivery로 변경
    * 포인트 적립
    * */

    @Transactional
    public void completeMockPayment(Long paymentId, CompletePaymentRequestDto requestDto) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EcomosException(ErrorCode._NOT_FOUND_PAYMENT));

        payment.completeWithOrder();

        Order order = payment.getOrder();
        User user = order.getUser();
        int originalAmount = order.getTotalPrice();
        int finalAmount = order.getTotalPrice();

        // 쿠폰 적용
        if(requestDto.getUserCouponId() != null) {
            UserCoupon userCoupon = userCouponRepository.findById(requestDto.getUserCouponId())
                    .orElseThrow(() -> new EcomosException(ErrorCode._NOT_FOUND_USER_COUPON));

            if(!userCoupon.getUser().getUid().equals(user.getUid())) {
                throw new EcomosException(ErrorCode._NOT_FOUND_COUPON);
            }

            if(userCoupon.isUsed() || userCoupon.isExpired()) {
                throw new EcomosException(ErrorCode._INVALID_COUPON);
            }

            Coupon coupon = userCoupon.getCoupon();
            if(originalAmount < coupon.getMinOrderAmount()) {
                throw new EcomosException(ErrorCode._NOT_ENOUGH_ORDER_PRICE_FOR_COUPON);
            }

            int discount;
            if(coupon.isPercent()) {
                discount = (int) (originalAmount * (coupon.getDiscountAmount() / 100.0));
            } else {
                discount = coupon.getDiscountAmount();
            }

            finalAmount -= discount;
            userCoupon.markAsUsed();;
        }

        // 포인트 사용
        if(requestDto.getUsePoint() > 0) {
            pointService.usePoint(user, requestDto.getUsePoint()); // 차감
            finalAmount -= requestDto.getUsePoint(); // 차감 반영
        }

        payment.setAmount(finalAmount); // 결제 금액 저장
        payment.completeWithOrder(); // 상태 전이

        // 주문 상태 변경
        order.setStatus(OrderStatus.WAITING_FOR_DELIVERY);

        // 포인트 적립
        int earned = (int)(payment.getAmount() * 0.05); // 5% 적립
        pointService.earnPoint(payment.getOrder().getUser(), earned);

        // 첫 주문 정챙
        couponPolicyRunner.run(user);
    }

    private PaymentSnapshot createSnapshotFromOrder(Order order) {

        PaymentSnapshot snapshot = new PaymentSnapshot();
        List<PaymentSnapshot.ItemSnapshot> items = new ArrayList<>();
        int total = 0;

        for (OrderItem item : order.getOrderItems()) {
            PaymentSnapshot.ItemSnapshot dto = new PaymentSnapshot.ItemSnapshot();
            dto.setProductName(item.getProduct().getName());
            dto.setUnitPrice(item.getPrice());
            dto.setQuantity(item.getQuantity());
            total += item.getPrice() * item.getQuantity();
            items.add(dto);
        }
        return snapshot;
    }

}
