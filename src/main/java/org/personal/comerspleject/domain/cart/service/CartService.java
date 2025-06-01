package org.personal.comerspleject.domain.cart.service;

import org.personal.comerspleject.domain.cart.dto.response.CartResponseDto;

public interface CartService {

    CartResponseDto addItemToCart(Long userId, Long productId, int quantity);

    CartResponseDto updateItemQuantity(Long userId, Long productId, int quantity);

    CartResponseDto deleteItemFromCart(Long userId, Long productId); // 특정 상품만 제거

    CartResponseDto getCart(Long userId);
}
