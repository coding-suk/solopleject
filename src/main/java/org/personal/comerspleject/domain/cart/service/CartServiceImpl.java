package org.personal.comerspleject.domain.cart.service;

import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.config.exception.EcomosException;
import org.personal.comerspleject.config.exception.ErrorCode;
import org.personal.comerspleject.domain.cart.dto.response.CartResponseDto;
import org.personal.comerspleject.domain.cart.entity.Cart;
import org.personal.comerspleject.domain.cart.entity.CartItem;
import org.personal.comerspleject.domain.cart.repository.CartRepository;
import org.personal.comerspleject.domain.users.seller.entity.Product;
import org.personal.comerspleject.domain.users.seller.repository.ProductRepository;
import org.personal.comerspleject.domain.users.user.entity.User;
import org.personal.comerspleject.domain.users.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    // 장바구니 상품 추가
    @Override
    public CartResponseDto addItemToCart(Long userId, Long productId, int quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new EcomosException(ErrorCode._NOT_FOUND_USER));

        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new EcomosException(ErrorCode._NOT_FOUND_PRODUCT));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(()-> new Cart(user));

        // 장바구니에 이미 있는 상품인지 확인
        Optional<CartItem> optionalItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getPId().equals(productId))
                .findFirst();

        if(optionalItem.isPresent()) {
            optionalItem.get().updateQuantity(optionalItem.get().getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem(product, quantity);
            cart.addItem(newItem);
        }

        cartRepository.save(cart);

        return CartResponseDto.from(cart);
    }

    // 전체 조회
    @Override
    public CartResponseDto getCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new EcomosException(ErrorCode._NOT_FOUND_USER));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(()-> new Cart(user));// 장바구니가 없는 경우 빈 장바구니 반환

        return CartResponseDto.from(cart);
    }

    // 장바구니에 담긴 상품 수량 수정
    @Override
    public CartResponseDto updateItemQuantity(Long userId, Long productId, int quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new EcomosException(ErrorCode._NOT_FOUND_USER));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(()-> new EcomosException(ErrorCode._EMPTY_CART));

        CartItem item = cart.getItems().stream()
                .filter(ci -> ci.getProduct().getPId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new EcomosException(ErrorCode._NOT_FOUND_PRODUCT_IN_CART));

        item.updateQuantity(quantity);
        cartRepository.save(cart);

        return CartResponseDto.from(cart);
    }

    @Override
    public CartResponseDto deleteItemFromCart(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EcomosException(ErrorCode._NOT_FOUND_USER));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new EcomosException(ErrorCode._EMPTY_CART));

        CartItem itemToRemove = cart.getItems().stream()
                .filter(item -> item.getProduct().getPId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new EcomosException(ErrorCode._NOT_FOUND_PRODUCT_IN_CART));

        cart.getItems().remove(itemToRemove);
        cartRepository.save(cart);

        return CartResponseDto.from(cart);
    }



}
