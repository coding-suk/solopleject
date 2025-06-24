package org.personal.comerspleject.domain.payment.repository;

import org.personal.comerspleject.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
