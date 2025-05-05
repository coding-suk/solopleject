package org.personal.comerspleject.domain.users.user.service;

import org.personal.comerspleject.config.exception.EcomosException;
import org.personal.comerspleject.config.exception.ErrorCode;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenService {

    private Map<String, String> tokenStore = new ConcurrentHashMap<>();
    private final long tokenValidityMillis = 30 * 60 *1000; // 30분 유효

    private final Map<String, Long> tokenExpiry = new ConcurrentHashMap<>();

    public String generatePasswordResetToken(String email) {
        String token = UUID.randomUUID().toString();
        tokenStore.put(token, email);
        tokenExpiry.put(token, System.currentTimeMillis() + tokenValidityMillis);
        return token;
    }

    public boolean isValid(String token) {
        return tokenStore.containsKey(token)
                && tokenExpiry.get(token) > System.currentTimeMillis();
    }

    public String getEmailFromToken(String token) {
        if (!isValid(token)) throw new EcomosException(ErrorCode._INVALID_TOKEN);
        return tokenStore.get(token);
    }

    public void removeToken(String token) {
        tokenStore.remove(token);
        tokenExpiry.remove(token);
    }

}
