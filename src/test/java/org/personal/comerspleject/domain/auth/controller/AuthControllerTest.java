package org.personal.comerspleject.domain.auth.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.personal.comerspleject.domain.auth.dto.request.SigninRequestDto;
import org.personal.comerspleject.domain.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("회원가입 후 로그인 성공 시 JWT 헤더 반환")
    void signup_and_signin_success() throws Exception {
        // mock 설정
        given(authService.signin(any(SigninRequestDto.class)))
                .willReturn("Bearer test.jwt.token");

        String signupJson = """
        {
          "email": "test@example.com",
          "password": "Test1234!",
          "name": "테스트유저",
          "role": "USER",
          "address": "서울특별시",
          "birthDate": "1995-05-05"
        }
        """;

        mockMvc.perform(post("/ecomos/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupJson))
                .andExpect(status().isOk());

        String signinJson = """
        {
          "email": "test@example.com",
          "password": "Test1234!"
        }
        """;

        mockMvc.perform(post("/ecomos/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signinJson))
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"));
    }

    /**
     * 실패 이유
     * 1. Json 파싱 오류
     * {}중솰호, 쉼표(,) 누락
     *
     * 2. DTO 유효성 검증 실패
     * dto로직에서 @NOTBLANK 붙은 필드를 누락
     *
     * 3. Authoriztion 헤더 없음
     * authService가 mockBean으로 등록되어 있고, signin결과를 설정하지 않음
     * -> givn(authService.signin())으로 mock 결과 명시
     */

}
