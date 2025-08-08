package org.personal.comerspleject.domain.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.personal.comerspleject.config.exception.EcomosException;
import org.personal.comerspleject.config.exception.ErrorCode;
import org.personal.comerspleject.config.jwt.JwtUtil;
import org.personal.comerspleject.domain.auth.dto.request.SigninRequestDto;
import org.personal.comerspleject.domain.auth.dto.request.SignupRequestDto;
import org.personal.comerspleject.domain.auth.entity.AuthUser;
import org.personal.comerspleject.domain.coupon.policyAndscheduler.CouponPolicyRunner;
import org.personal.comerspleject.domain.coupon.repository.UserCouponRepository;
import org.personal.comerspleject.domain.order.dto.response.RecentOrderResponseDto;
import org.personal.comerspleject.domain.order.entity.Order;
import org.personal.comerspleject.domain.order.repository.OrderRepository;
import org.personal.comerspleject.domain.users.user.dto.response.UserInfoResponseDto;
import org.personal.comerspleject.domain.users.user.entity.User;
import org.personal.comerspleject.domain.users.user.entity.UserRole;
import org.personal.comerspleject.domain.users.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final CouponPolicyRunner couponPolicyRunner;
    private final OrderRepository orderRepository;

    // 이메일 유효성 검사 정규 표현식
    private static final String EMAIL_PATTERN =
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    // 비밀번호 유효성 검사 정규 표현식
    private static final String PASSWORD_PATTERN =
            "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*()_+=\\-\\[\\]{};':\"\\\\|,.<>/?`~]{8,}$";
    private final UserCouponRepository userCouponRepository;

    // 회원가입
    @Transactional
    public void signup(SignupRequestDto signupRequestDto) {
        try{
            log.info("➡️ 회원가입 요청 수신: {}", signupRequestDto);
        } catch(Exception e) {
            e.printStackTrace();
            throw new EcomosException(ErrorCode._INTERNAL_SERVER_ERROR);
        }


        // 이메일 형식 유효성 검사
        if(!isValidEmail(signupRequestDto.getEmail())) {
            log.warn("⛔ 잘못된 이메일 형식: {}", signupRequestDto.getEmail());
            throw new EcomosException(ErrorCode._BAD_REQUEST_INVALID_EMAIL);
        }

        // 이메일 중복확인
        if(userRepository.existsByEmail(signupRequestDto.getEmail())) {
            log.warn("⛔ 중복된 이메일: {}", signupRequestDto.getEmail());
            throw new EcomosException(ErrorCode._DUPLICATED_EMAIL);
        }

        // 비밀번호 형식 유효성 검사
        if(!isValidPassword(signupRequestDto.getPassword())) {
            log.warn("⛔ 비밀번호 형식 불일치");
            throw new EcomosException(ErrorCode._INVALID_PASSWORD_FORM);
        }

        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());
        UserRole role = UserRole.of(signupRequestDto.getRole());

        log.info("회원 객체 생성 중...");

        User newUser = new User(
                signupRequestDto.getEmail(),
                encodedPassword,
                signupRequestDto.getName(),
                role,
                signupRequestDto.getAddress(),
                signupRequestDto.getBirthDate()
        );
        // 유저 생성 후 저장
        userRepository.save(newUser);
        log.info("✅ 유저 저장 완료: {}", newUser.getEmail());

        // 쿠폰 자동 발급
        try {
            couponPolicyRunner.run(newUser);
            log.info("🎁 쿠폰 자동 발급 완료");
        } catch (Exception e) {
            log.error("❌ 쿠폰 발급 중 예외 발생", e);
            throw new EcomosException(ErrorCode._INTERNAL_SERVER_ERROR); // or define a specific coupon-related error
        }
    }

    // 이메일 유효성 검사 메서드
    private boolean isValidEmail(String email) {
        return Pattern.matches(EMAIL_PATTERN, email);
    }

    // 비밀번호 유효성 검사 메서드
    private boolean isValidPassword(String password) {
        return Pattern.matches(PASSWORD_PATTERN, password);
    }

    // 로그인
    @Transactional
    public String signin(SigninRequestDto signinRequestDto) {
        User user = userRepository.findByEmail(signinRequestDto.getEmail()).orElseThrow(
                () -> new EcomosException(ErrorCode._NOT_FOUND_USER));

        // 로그인 시 이메일과 비밀번호가 일치하지 않을 경우 401을 반환
        if(!passwordEncoder.matches(signinRequestDto.getPassword(), user.getPassword())) {
            throw new EcomosException(ErrorCode._PASSWORD_NOT_MATCHES);
        }

        // 탈퇴한 유저일 경우 로그인 불가
        if(user.isDeleted()) {
            throw new EcomosException(ErrorCode._DELETED_USER);
        }

        return jwtUtil.createToken(user.getUid(), user.getEmail(), user.getName(), user.getRole());
    }

    // 로그인 유지
    @Transactional(readOnly = true)
    public UserInfoResponseDto getMyInfo(AuthUser authUSer) {
        User user = userRepository.findByEmail(authUSer.getEmail())
                .orElseThrow(()-> new EcomosException(ErrorCode._NOT_FOUND_USER));

        // 보유 쿠폰 수
        int couponCount = userCouponRepository.countActivateCoupons(user);

        // 최근 주문
        Order order = orderRepository.findTopByUserOrderByCreatedAtDesc(user)
                .orElse(null);

        RecentOrderResponseDto recentOrderDto = null;
        if (order != null) {
            List<String> productNames = order.getOrderItems().stream()
                    .map(oi -> oi.getProduct().getName())
                    .toList();

            recentOrderDto = new RecentOrderResponseDto(
                    order.getOId(),
                    order.getCreatedAt(),
                    order.getTotalPrice(),
                    productNames
            );
        }

        return new UserInfoResponseDto(
                user.getUid(),
                user.getName(),
                user.getEmail(),
                user.getAddress(),
                user.getRole(),
                user.getPoint(),
                user.getCreatedAt(),
                couponCount,
                (order != null ? order.getCreatedAt() : null),
                recentOrderDto
        );
    }
}
