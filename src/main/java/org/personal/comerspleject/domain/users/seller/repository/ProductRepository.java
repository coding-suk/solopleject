package org.personal.comerspleject.domain.users.seller.repository;

import org.personal.comerspleject.domain.users.seller.entity.Product;
import org.personal.comerspleject.domain.users.seller.entity.ProductStatus;
import org.personal.comerspleject.domain.users.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByIsDeletedFalse();

    //판매자별 상품 조회
    List<Product> findBySellerId(Long sellerId);

    List<Product> findBySellerAndIsDeletedFalse(User seller);

    List<Product> findBySellerUidAndIsDeletedFalse(Long sellerId);

    // 승인된 상품만 조회
    List<Product> findAllByStatus(Product status);

    // 블라인드 처리되지 않은 상품만 보기로 추가
    List<Product> findAllByStatusAndIsBlindFalse(ProductStatus status);
}
