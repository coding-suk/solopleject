package org.personal.comerspleject.domain.cart.service;

import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.config.exception.EcomosException;
import org.personal.comerspleject.config.exception.ErrorCode;
import org.personal.comerspleject.domain.cart.dto.request.CartItemMergeRequestDto;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    // 사용자키 구성 메서드
    private String userKey(Long userId) {
        return "cart:" + userId;
    }

    // 게스트키 구성
    private String guestKey(String guestId) {
        return "cart:guest:" + guestId;
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

            cart.getItems().stream()
                    .filter(item -> item.getProduct().getPId().equals(productId))
                    .findFirst()
                    .ifPresentOrElse(
                            it -> it.updateQuantity(it.getQuantity() + quantity),
                            () -> cart.addItem(new CartItem(product, quantity))
                    );

            cartRepository.save(cart);
            // 사용자 캐쉬 무효화
            redisTemplate.delete(userKey(userId));

//            // 장바구니에 이미 있는 상품인지 확인
//            Optional<CartItem> optionalItem = cart.getItems().stream()
//                    .filter(item -> item.getProduct().getPId().equals(productId))
//                    .findFirst();
//
//            if(optionalItem.isPresent()) {
//                optionalItem.get().updateQuantity(optionalItem.get().getQuantity() + quantity);
//            } else {
//                CartItem newItem = new CartItem(product, quantity);
//                cart.addItem(newItem);
//            }
//
//            cartRepository.save(cart);
//
//            // redis 캐쉬 삭제
//            redisTemplate.delete(userKey(userId));

            return CartResponseDto.from(cart);
        }

    // 전체 조회
    @Override
    public CartResponseDto getCart(Long userId) {

        String redisKey = userKey(userId);

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

        // redis 캐시 삭제(사용자 캐쉬 무효화)
        redisTemplate.delete(userKey(userId));

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
        redisTemplate.delete(userKey(userId));

        return CartResponseDto.from(cart);
    }

    // 저장 메서드, 비회원 장바구니를 redis 서버에 저장
    @Override
    public void saveCartInRedis(String guestId, List<CartItemMergeRequestDto> items) {

        String key = "guest_cart:" + guestId;
        // 리스트 자체를 값으로 저장(Json 직렬화)
        redisTemplate.opsForValue().set(key, items, Duration.ofHours(2)); // TTL 2시간
    }

    // 로그인 시 redis에서 게스트 카트 불러와 병합 -> 게스트 키 삭제
    public void mergeGuestCartToUser(String guestId, Long userId) {
        String gKey = guestKey(guestId);

        @SuppressWarnings("unchecked")
        List<CartItemMergeRequestDto> guestItems =
                (List<CartItemMergeRequestDto>) redisTemplate.opsForValue().get(gKey);

        if(guestItems == null || guestItems.isEmpty()) {
            // 게스트 장바구니가 없거나 만료됨
            redisTemplate.delete(gKey); // 남아있으면 정리
            return;
        }

        // DB병합
        mergeCart(userId, guestItems);

        // 게스트 키 삭제
        redisTemplate.delete(gKey);
    }

    // 기존 병합 로직(클라이언트가 리스트를 직접 보낸 경우도 지원)

    @Override
    public void mergeCart(Long userId, List<CartItemMergeRequestDto> guestItems) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EcomosException(ErrorCode._NOT_FOUND_USER));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> new Cart(user));

        for (CartItemMergeRequestDto dto : guestItems) {
            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new EcomosException(ErrorCode._NOT_FOUND_PRODUCT));

            Optional<CartItem> existing = cart.getItems().stream()
                    .filter(item -> item.getProduct().getPId().equals(product.getPId()))
                    .findFirst();

            if (existing.isPresent()) {
                existing.get().updateQuantity(existing.get().getQuantity() + dto.getQuantity());
            } else {
                cart.addItem(new CartItem(product, dto.getQuantity()));
            }
        }

        cartRepository.save(cart);
        redisTemplate.delete(userKey(userId));
    }

}
