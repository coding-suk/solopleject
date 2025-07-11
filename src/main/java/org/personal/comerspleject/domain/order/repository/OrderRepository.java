package org.personal.comerspleject.domain.order.repository;

import org.personal.comerspleject.domain.order.entity.Order;
import org.personal.comerspleject.domain.users.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUser(User user);

    long countByUser(User user);
}
