package org.personal.comerspleject.domain.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.domain.auth.dto.request.SigninRequestDto;
import org.personal.comerspleject.domain.auth.dto.request.SignupRequestDto;
import org.personal.comerspleject.domain.auth.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
