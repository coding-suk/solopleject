package org.personal.comerspleject.domain.users.seller.controller;

import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.domain.users.seller.dto.request.CreateProductRequestDto;
import org.personal.comerspleject.domain.users.seller.dto.response.ProductDetailResponseDto;
import org.personal.comerspleject.domain.users.seller.dto.response.ProductResponseDto;
import org.personal.comerspleject.domain.users.seller.service.ProductService;
import org.personal.comerspleject.domain.users.user.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ecomos/sellers/")
public class ProductController {

    private final ProductService productService;

    // 등록(판매자)
    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody CreateProductRequestDto createProductRequestDto,
                                                @AuthenticationPrincipal User seller) {
        productService.createProduct(createProductRequestDto, seller);
        return ResponseEntity.ok("상품이 등록되었습니다");
    }

    // 조회 (모든 유저)
    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // 특정 상품 상세 조회(모든 유저)
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailResponseDto> getProductDetail(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProduct(productId));
    }

    // 본인 상품 목록조회(판매자)
    @GetMapping("/my")
    public ResponseEntity<List<ProductResponseDto>> getMyProduct(@AuthenticationPrincipal User seller) {
        return ResponseEntity.ok(productService.getMyProducts(seller));
    }

    // 특정 판매자의 상품 목록 조회 (관리자 전용)
    @GetMapping("/admin/{sellerId}")
    public ResponseEntity<List<ProductResponseDto>> getProductsBySellerId(@PathVariable Long sellerId) {
        return ResponseEntity.ok(productService.getProductBySellerId(sellerId));
    }

    // 상품 수정 (판매자 전용)
    @PutMapping("/{productId}")
    public ResponseEntity<String> updateProduct(@PathVariable Long productId,
                                                @RequestBody CreateProductRequestDto requestDto,
                                                @AuthenticationPrincipal User seller) {
        productService.updateProduct(productId, requestDto, seller);
        return ResponseEntity.ok("상품이 수정되었습니다");
    }

    // 상품 삭제 (판매자 전용)
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId,
                                                @AuthenticationPrincipal User seller) {
        productService.deleteProduct(productId, seller);
        return ResponseEntity.ok("상품이 삭제되었습니다");
    }

}
