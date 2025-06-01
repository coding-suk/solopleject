package org.personal.comerspleject.domain.cart.repository;

import org.personal.comerspleject.domain.cart.entity.Cart;
import org.personal.comerspleject.domain.users.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(User user);
}
