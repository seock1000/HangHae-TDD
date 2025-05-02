package kr.hhplus.be.server.config.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
@Slf4j
public class LockTemplateImpl implements LockTemplate {
    private static final String REDISSON_LOCK_PREFIX = "LOCK:";

    private final RedissonClient redissonClient;
    private final LettuceLockManager lettuceLockManager;

    @Override
    public <T> T simpleLock(String key, Supplier<T> supplier) throws IllegalStateException {
        if (!lettuceLockManager.lock(key)) {
            throw new IllegalStateException("RESOURCE LOCKED : 자원이 현재 점유되고 있어 요청에 실패했습니다.");
        }
        try {
            return supplier.get();
        } finally {
            log.info("lettuceLockManager unlock (key : {})", key);
            try {
                lettuceLockManager.unlock(key);
            } catch (IllegalMonitorStateException e) {
                log.info("Lettuce Lock Already UnLock (key : {})", key);
            }
        }
    }

    @Override
    public <T> T spinLock(String key, Supplier<T> supplier) throws InterruptedException {
        while (true) {
            if (!lettuceLockManager.lock(key)) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new InterruptedException("RESOURCE LOCKED : 자원이 현재 점유되고 있어 요청에 실패했습니다.");
                }
            } else {
                break;
            }
        }
        try {
            return supplier.get();
        } finally {
            log.info("lettuceLockManager unlock (key : {})", key);
            try {
                lettuceLockManager.unlock(key);
            } catch (IllegalMonitorStateException e) {
                log.info("Lettuce Lock Already UnLock (key : {})", key);
            }
        }
    }

    @Override
    public <T> T pubSubLock(String key, long waitTime, long releaseTime, TimeUnit timeUnit, Supplier<T> supplier) throws InterruptedException {
        RLock rLock = redissonClient.getLock(key);
        try {
            boolean available = rLock.tryLock(waitTime, releaseTime, timeUnit);
            if (!available) {
                throw new IllegalStateException("RESOURCE LOCKED : 자원이 현재 점유되고 있어 요청에 실패했습니다.");
            }
            return supplier.get();

        } catch (InterruptedException e) {
            throw new InterruptedException();
        } finally {
            log.info("Redisson Lock UNLOCK (key : {})", key);
            try {
                rLock.unlock();
            } catch (IllegalMonitorStateException e) {
                log.info("Redisson Lock Already UnLock (key : {})", key);
            }
        }
    }
}
