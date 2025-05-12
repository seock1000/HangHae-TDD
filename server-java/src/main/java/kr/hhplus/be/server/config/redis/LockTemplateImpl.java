package kr.hhplus.be.server.config.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
        key = REDISSON_LOCK_PREFIX + key;
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
        key = REDISSON_LOCK_PREFIX + key;
        while (!lettuceLockManager.lock(key)) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new InterruptedException("RESOURCE LOCKED : 자원이 현재 점유되고 있어 요청에 실패했습니다.");
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
    public <T> T pubSubLock(String key, long waitTime, long releaseTime, TimeUnit timeUnit, Supplier<T> supplier) {
        key = REDISSON_LOCK_PREFIX + key;
        RLock rLock = redissonClient.getLock(key);
        try {
            boolean available = rLock.tryLock(waitTime, releaseTime, timeUnit);
            if (!available) {
                throw new IllegalStateException("RESOURCE LOCKED : 자원이 현재 점유되고 있어 요청에 실패했습니다.");
            }
            return supplier.get();

        } catch (InterruptedException e) {
            throw new IllegalStateException("RESOURCE LOCKED : 자원이 현재 점유되고 있어 요청에 실패했습니다.");
        } finally {
            log.info("Redisson Lock UNLOCK (key : {})", key);
            try {
                rLock.unlock();
            } catch (IllegalMonitorStateException e) {
                log.info("Redisson Lock Already UnLock (key : {})", key);
            }
        }
    }

    @Override
    public <T> T executeWithLocks(List<LockCommand> locks, Supplier<T> supplier) {
        List<Runnable> unlockStack = new ArrayList<>();
        try {
            for (LockCommand lock : locks) {
                String key = REDISSON_LOCK_PREFIX + lock.key();

                switch (lock.method()) {
                    case SIMPLE, SPIN -> {
                        boolean available = false;
                        if (lock.method() == LockMethod.SIMPLE) {
                            available = lettuceLockManager.lock(key);
                        } else {
                            while (!available) {
                                available = lettuceLockManager.lock(key);
                                try {
                                    if (!available) Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    throw new IllegalStateException("RESOURCE LOCKED : 자원이 현재 점유되고 있어 요청에 실패했습니다.");
                                }
                            }
                        }
                        if (!available) throw new IllegalStateException("RESOURCE LOCKED : 자원이 현재 점유되고 있어 요청에 실패했습니다.");
                        unlockStack.add(() -> {
                            lettuceLockManager.unlock(key);
                            log.info("lettuceLockManager unlock (key : {})", key);
                        });
                    }
                    case PUBSUB -> {
                        RLock rLock = redissonClient.getLock(key);
                        try {
                            boolean available = rLock.tryLock(lock.waitTime(), lock.leaseTime(), lock.timeUnit());
                            if (!available)
                                throw new IllegalStateException("RESOURCE LOCKED : 자원이 현재 점유되고 있어 요청에 실패했습니다.");
                            unlockStack.add(() -> {
                                try {
                                    rLock.unlock();
                                    log.info("Redisson Lock UNLOCK (key : {})", key);
                                } catch (IllegalMonitorStateException e) {
                                    log.info("Redisson Lock Already UnLock (key : {})", key);
                                }
                            });
                        } catch (InterruptedException e) {
                            throw new IllegalStateException("RESOURCE LOCKED : 자원이 현재 점유되고 있어 요청에 실패했습니다.");
                        }
                    }
                    default -> throw new IllegalArgumentException("LOCK METHOD NOT FOUND : 지원하지 않는 락 방식입니다.");
                }
                log.info("LOCK SUCCESS : 락을 성공적으로 획득했습니다. key : {}", key);
            }
            return supplier.get();

        } finally {
            Collections.reverse(unlockStack); // 락 해제 역순
            unlockStack.forEach(Runnable::run);
        }
    }
}