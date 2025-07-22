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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    // redis 키 구성 메서드
    private String getRedisKey(Long userId) {
        return "cart:" + userId;
    }

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

        // redis 캐쉬 삭제
        redisTemplate.delete(getRedisKey(userId));

        return CartResponseDto.from(cart);
    }

    // 전체 조회
    @Override
    public CartResponseDto getCart(Long userId) {

        String redisKey = getRedisKey(userId);

        // redis에서 먼저 조회
        CartResponseDto cached = (CartResponseDto) redisTemplate.opsForValue().get(redisKey);
        if(cached != null) {
            return cached;
        }

        // redis에 없으면 DB에 조회
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new EcomosException(ErrorCode._NOT_FOUND_USER));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(()-> new Cart(user));// 장바구니가 없는 경우 빈 장바구니 반환

        CartResponseDto response = CartResponseDto.from(cart);

        // 성능 최적화 : 비어있는 장바구니는 캐시하지 않음
        if(response.getItems() != null && !response.getItems().isEmpty()) {
            // redis 캐시 저장
            redisTemplate.opsForValue().set(redisKey, response, Duration.ofMinutes(30));
        }

        return response;
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

        // redis 캐시 삭제
        redisTemplate.delete(getRedisKey(userId));

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

        // redis 캐시 삭제
        redisTemplate.delete(getRedisKey(userId));

        return CartResponseDto.from(cart);
    }



}
