package kr.hhplus.be.server.config.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class LettuceLockManager {

    private final RedisTemplate<String,String> redisTemplate;

    public Boolean lock(String key){
        return redisTemplate
                .opsForValue()
                .setIfAbsent(key, "lock", Duration.ofMillis(3_000)); // 3_000ms
    }

    public Boolean unlock(String key) {
        return redisTemplate.delete(key);
    }
}

