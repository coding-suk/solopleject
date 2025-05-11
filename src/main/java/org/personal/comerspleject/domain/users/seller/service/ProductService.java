package org.personal.comerspleject.domain.users.seller.service;

import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.config.exception.EcomosException;
import org.personal.comerspleject.config.exception.ErrorCode;
import org.personal.comerspleject.domain.users.seller.dto.request.CreateProductRequestDto;
import org.personal.comerspleject.domain.users.seller.dto.response.ProductDetailResponseDto;
import org.personal.comerspleject.domain.users.seller.dto.response.ProductResponseDto;
import org.personal.comerspleject.domain.users.seller.entity.Product;
import org.personal.comerspleject.domain.users.seller.repository.ProductRepository;
import org.personal.comerspleject.domain.users.user.entity.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class ProductService {

    private final ProductRepository productRepository;

    //등록
    @PreAuthorize("hasRole('SELLER')")
    public void createProduct(CreateProductRequestDto createProductRequestDto, User seller) {
        Product product = new Product();
        product.setName(createProductRequestDto.getName());
        product.setDescription(createProductRequestDto.getDescription());
        product.setPrice(createProductRequestDto.getPrice());
        product.setStock(createProductRequestDto.getStock());
        product.setCategory(createProductRequestDto.getCategory());
        product.setImageURL(createProductRequestDto.getImageURL());
        product.setSeller(seller); // 판매자 연결
        productRepository.save(product);
    }

    // 전체 상품 조회(리스트)
    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAllByIsDeletedFalse()
                .stream()
                .map(p -> new ProductResponseDto(p.getPId(), p.getName(), p.getPrice(), p.getCategory(), p.getImageURL()))
                .collect(Collectors.toList());
    }

    // 단건 상품 조회
    public ProductDetailResponseDto getProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .filter(p -> !p.isDeleted())
                .orElseThrow(() -> new EcomosException(ErrorCode._NOT_FOUND_PRODUCT));

        return new ProductDetailResponseDto(product);
    }

    // 판매자 본인 상품반 조회
    @PreAuthorize("hasRole('SELLER')")
    public List<ProductResponseDto> getMyProducts(User seller) {
        return productRepository.findBySellerAndIsDeletedFalse(seller)
                .stream()
                .map(p -> new ProductResponseDto(p.getPId(), p.getName(), p.getPrice(), p.getCategory(), p.getImageURL()))
                .collect(Collectors.toList());
    }

    public List<ProductResponseDto> getProductBySellerId(Long sellerId) {
        List<Product> products = productRepository.findBySellerUidAndIsDeletedFalse(sellerId);
        return products.stream()
                .map(p-> new ProductResponseDto(p.getPId(), p.getName(), p.getPrice(), p.getCategory(), p.getImageURL()))
                .collect(Collectors.toList());
    }

    // 상품 수정
    @Transactional
    @PreAuthorize("hasRole('SELLER')")
    public void updateProduct(Long productId, CreateProductRequestDto createProductRequestDto, User seller) {
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new EcomosException(ErrorCode._NOT_FOUND_PRODUCT));

        if (!product.getSeller().getUid().equals(seller.getUid())) {
            throw new EcomosException(ErrorCode._FORBIDDEN);
        }

        product.update(
                createProductRequestDto.getName(),
                createProductRequestDto.getDescription(),
                createProductRequestDto.getPrice(),
                createProductRequestDto.getStock(),
                createProductRequestDto.getCategory(),
                createProductRequestDto.getImageURL()
        );
    }

    // 상품 삭제
    @Transactional
    @PreAuthorize("hasRole('SELLER')")
    public void deleteProduct(Long productId, User seller) {
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new EcomosException(ErrorCode._NOT_FOUND_PRODUCT));

        if(!product.getSeller().getUid().equals(seller.getUid())) {
            throw new EcomosException(ErrorCode._FORBIDDEN);
        }

        product.markAsDeleted();
    }
}
