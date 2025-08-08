package org.personal.comerspleject.domain.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.personal.comerspleject.config.exception.EcomosException;
import org.personal.comerspleject.config.exception.ErrorCode;
import org.personal.comerspleject.domain.auth.dto.request.SigninRequestDto;
import org.personal.comerspleject.domain.auth.dto.request.SignupRequestDto;
import org.personal.comerspleject.domain.auth.entity.AuthUser;
import org.personal.comerspleject.domain.auth.service.AuthService;
import org.personal.comerspleject.domain.users.user.dto.response.UserInfoResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/ecomos/auth")
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/signup")
    public String signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        authService.signup(signupRequestDto);
        return "회원가입 완료";
    }

    // 로그인
    @PostMapping("/signin")
    public ResponseEntity<String> signin(@Valid @RequestBody SigninRequestDto signinRequestDto) {
        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, authService.signin(signinRequestDto)).body("SUCCESS");
    }

    // 로그인 유지
    @GetMapping("/me")
    public ResponseEntity<UserInfoResponseDto> getCurrentUser(@AuthenticationPrincipal AuthUser authUser) {
        try {
            if (authUser == null) {
                log.error("❌ AuthUser가 null입니다!");
                throw new EcomosException(ErrorCode._INVALID_TOKEN);
            }

            UserInfoResponseDto dto = authService.getMyInfo(authUser);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("🔥 /me 요청 처리 중 예외 발생: {}", e.getMessage(), e);
            throw e;
        }
    }
}
