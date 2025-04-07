package org.personal.comerspleject.config.security;

import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.config.jwt.JwtSecurityFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtSecurityFilter jwtSecurityFilter;

}
