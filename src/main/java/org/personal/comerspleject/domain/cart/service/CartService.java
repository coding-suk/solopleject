package org.personal.comerspleject.domain.cart.service;

import org.personal.comerspleject.domain.cart.dto.request.CartItemMergeRequestDto;
import org.personal.comerspleject.domain.cart.dto.response.CartResponseDto;
import org.personal.comerspleject.domain.cart.entity.CartItem;

import java.util.List;

public interface CartService {

    CartResponseDto addItemToCart(Long userId, Long productId, int quantity);

    CartResponseDto updateItemQuantity(Long userId, Long productId, int quantity);

    CartResponseDto deleteItemFromCart(Long userId, Long productId); // 특정 상품만 제거

    CartResponseDto getCart(Long userId);

    // 병합 로직
    void mergeCart(Long guestId, List<CartItemMergeRequestDto> guestItems);

    //저장 메서드 구현
    void saveCartInRedis(String userID, List<CartItemMergeRequestDto> item);
}
