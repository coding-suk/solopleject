package org.personal.comerspleject.domain.users.seller.service;

import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.domain.users.seller.entity.Product;
import org.personal.comerspleject.domain.users.seller.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminProductService {

    private final ProductRepository productRepository;

    public void deleteProductByAdmin(Long productId) {
        Product product = getProductOrThrow(productId);
        productRepository.delete(product);
    }

    public void blindProduct(Long productId) {
        Product product = getProductOrThrow(productId);
        product.blind();
        productRepository.save(product);
    }

    public void approveProduct(Long productId) {
        Product product = getProductOrThrow(productId);
        product.approve();
        productRepository.save(product);
    }

    public void rejectProduct(Long productId) {
        Product product = getProductOrThrow(productId);
        product.reject();
        productRepository.save(product);
    }

    private Product getProductOrThrow(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
    }
}
