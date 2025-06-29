package org.personal.comerspleject.domain.payment.controller;

import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.domain.payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ecomos/payments")
public class PaymentController {

    private final PaymentService paymentService;

    // 결제 준비
    @PostMapping("initate/{orderId}")
    public ResponseEntity<Long> initiatePayment(@PathVariable Long orderId) {
        Long paymentId = paymentService.initiateMockPayment(orderId);
        return ResponseEntity.ok(paymentId);
    }

    // 결제 완료
    @PostMapping("/complete/{paymentId}")
    public ResponseEntity<String> completePayment(@PathVariable Long paymentId,
                                                  @RequestParam int usePoint) {
        paymentService.completeMockPayment(paymentId, usePoint);
        return ResponseEntity.ok("결제가 완료 되었습니다");
    }

}
