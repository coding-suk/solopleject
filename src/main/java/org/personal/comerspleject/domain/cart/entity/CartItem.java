package org.personal.comerspleject.domain.cart.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.personal.comerspleject.domain.users.seller.entity.Product;

@Data
@Entity
@NoArgsConstructor
public class CartItem {

    // 개별 상품
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ciId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }


}
