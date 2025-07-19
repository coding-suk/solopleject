package org.personal.comerspleject.domain.users.seller.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.personal.comerspleject.config.exception.EcomosException;
import org.personal.comerspleject.config.exception.ErrorCode;
import org.personal.comerspleject.domain.users.user.entity.User;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Getter
@Entity
@Data
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pId;

    private String name;

    private String description;

    private Integer price;

    private Integer stock;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    private String imageURL;

    private boolean isDeleted = false;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_uid", nullable = false)
    private User seller;

    private boolean isBlind = false;

    @Enumerated(EnumType.STRING)
    private ProductStatus status = ProductStatus.PENDING;

    public void update(String name, String description, Integer price, Integer stock, ProductCategory category, String imageURL) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.imageURL = imageURL;
    }

    public boolean isDeleted() {
        return this.isDeleted;
    }

    public void markAsDeleted() {
        this.isDeleted = true;
    }

    // 재고 감소
    public void decreaseStock(int quantity) {
        if(this.stock < quantity) {
            throw new EcomosException(ErrorCode._OUT_OF_STOCK);
        }
        this.stock -= quantity;
    }

    //
    public void blind() {
        this.isBlind = true;
    }

    public void approve() {
        this.status = ProductStatus.APPROVED;
    }

    public void reject() {
        this.status = ProductStatus.REJECTED;
    }

}
