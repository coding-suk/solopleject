package org.personal.comerspleject.config.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.personal.comerspleject.config.exception.EcomosException;
import org.personal.comerspleject.config.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private final String BEARER_PREFIX = "Bearer ";
    private final long TOKEN_TIME = 60 * 120 * 1000L; // 토큰 유효시간 120분

    @Value("${JWT_SECRET}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 토큰 생성
    public String createToken(Long userId, String email, String name, UserRole role) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(String.valueOf(userId))
                        .claim("email", email)
                        .claim("name", name)
                        .claim("role", role)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 토큰 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

    // 토큰 추출 7자리 쳐내기
    public String substringToken(String tokenValue) {
        if(StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        throw new EcomosException(ErrorCode._NOT_FOUND_TOKEN); // 토큰이 없거나 유효하지 않을 때
    }
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder() // JwtParseBuilder 생성
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
