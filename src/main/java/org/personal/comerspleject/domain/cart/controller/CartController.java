package org.personal.comerspleject.domain.cart.controller;

import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.domain.auth.entity.AuthUser;
import org.personal.comerspleject.domain.cart.dto.request.AddCartItemRequestDto;
import org.personal.comerspleject.domain.cart.dto.request.UpdateCartItemRequestDto;
import org.personal.comerspleject.domain.cart.dto.response.CartResponseDto;
import org.personal.comerspleject.domain.cart.service.CartServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ecomos/cart")
public class CartController {

    private final CartServiceImpl cartService;


    // 장바구니 조회
    @GetMapping
    public ResponseEntity<CartResponseDto> getCart(@AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok(cartService.getCart(authUser.getId()));
    }

    // 장바구니에 상품 추가
    @PostMapping
    public ResponseEntity<CartResponseDto> addToCart(@AuthenticationPrincipal AuthUser authUser,
                                            @RequestBody AddCartItemRequestDto addRequestDto) {
        CartResponseDto responseDto = cartService.addItemToCart(authUser.getId(), addRequestDto.getProductId(), addRequestDto.getQuantity());
        return ResponseEntity.ok(responseDto);
    }

    // 장바구니에서 상품 삭제
    @PatchMapping("/items/{productId}")
    public ResponseEntity<CartResponseDto> updateCartItem(@AuthenticationPrincipal AuthUser authUser,
                                                          @PathVariable Long productId,
                                                          @RequestBody UpdateCartItemRequestDto requestDto) {
        CartResponseDto responseDto = cartService.updateItemQuantity(authUser.getId(), productId, requestDto.getQuantity());
        return ResponseEntity.ok(responseDto);
    }

    // 장바구니에서 상품 삭제
    @DeleteMapping("/items/{productId}")
    public ResponseEntity<CartResponseDto> deleteCartItem(@AuthenticationPrincipal AuthUser authUser,
                                                          @PathVariable Long productId) {
        CartResponseDto response = cartService.deleteItemFromCart(authUser.getId(), productId);
        return ResponseEntity.ok(response);
    }
}
