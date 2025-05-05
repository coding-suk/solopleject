package org.personal.comerspleject.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.personal.comerspleject.config.exception.EcomosException;
import org.personal.comerspleject.config.exception.ErrorCode;
import org.personal.comerspleject.domain.auth.entity.AuthUser;
import org.personal.comerspleject.domain.users.user.entity.UserRole;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtSecurityFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            //JWT 토큰 검증, 사용자 인증 확인
            HttpServletRequest httpRequest,
            @NonNull HttpServletResponse httpResponse,
            @NonNull FilterChain chain
    ) throws ServletException, IOException {
        String authorizationHeader = httpRequest.getHeader("Authorization");
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = jwtUtil.substringToken(authorizationHeader);
            try {
                Claims claims = jwtUtil.extractClaims(jwt);
                Long userId = Long.parseLong(claims.getSubject());
                String name = claims.get("name", String.class);
                String email = claims.get("email", String.class);
                UserRole userRole = UserRole.of(claims.get("role",String.class));
                String address = claims.get("address", String.class);

                if(SecurityContextHolder.getContext().getAuthentication() == null) {
                    AuthUser authUser = new AuthUser(userId, name, email, userRole, address);

                    // 권한 부여
                    List<SimpleGrantedAuthority> authorities =
                            List.of(new SimpleGrantedAuthority("ROLE_" + userRole.name()));

                    // 권한 포함한 인증 객체 생성
                    JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(authUser);
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));

                    // 시큐리티 컨텍스트에 등록
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch(SecurityException | MalformedJwtException e) {
                throw new EcomosException(ErrorCode._UNAUTHORIZED_INVALID_TOKEN);
            } catch(ExpiredJwtException e) {
                throw new EcomosException(ErrorCode._UNAUTHORIZED_EXPIRED_TOKEN);
            } catch (UnsupportedJwtException e) {
                throw new EcomosException(ErrorCode._BAD_REQUEST_UNSUPPORTED_TOKEN);
            }
        }
        chain.doFilter(httpRequest, httpResponse);
    }
}
