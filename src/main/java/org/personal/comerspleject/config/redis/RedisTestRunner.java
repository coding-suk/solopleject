package org.personal.comerspleject.config.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisTestRunner implements CommandLineRunner {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void run(String...args) {

        try {
            redisTemplate.opsForValue().set("ping", "pong", Duration.ofSeconds(5));
            System.out.println("✅ Redis 연결 성공: " + redisTemplate.opsForValue().get("ping"));
        } catch (Exception e) {
            System.out.println("❌ Redis 연결 실패: " + e.getMessage());
        }
    }
}
