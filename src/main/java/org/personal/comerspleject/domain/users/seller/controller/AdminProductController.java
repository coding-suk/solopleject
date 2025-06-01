package org.personal.comerspleject.domain.users.seller.controller;

import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.domain.users.seller.service.AdminProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ecomos/admin/products")
public class AdminProductController {

    private final AdminProductService adminProductService;

    // 상품 삭제
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        adminProductService.deleteProductByAdmin(productId);
        return ResponseEntity.ok("상품이 삭제되었습니다.");
    }

    // 상품 블라인드
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{productId}/blind")
    public ResponseEntity<String> blindProduct(@PathVariable Long productId) {
        adminProductService.blindProduct(productId);
        return ResponseEntity.ok("상품이 블라인드 처리되었습니다.");
    }

    // 상품 승인
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{productId}/approve")
    public ResponseEntity<String> approveProduct(@PathVariable Long productId) {
        adminProductService.approveProduct(productId);
        return ResponseEntity.ok("상품이 승인되었습니다.");
    }

    // 상품 반려
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{productId}/reject")
    public ResponseEntity<String> rejectProduct(@PathVariable Long productId) {
        adminProductService.rejectProduct(productId);
        return ResponseEntity.ok("상품이 반려되었습니다.");
    }

}
