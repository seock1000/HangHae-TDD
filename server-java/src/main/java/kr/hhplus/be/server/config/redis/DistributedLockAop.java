package kr.hhplus.be.server.config.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class DistributedLockAop {
    private static final String REDISSON_LOCK_PREFIX = "LOCK:";

    private final RedissonClient redissonClient;
    private final LettuceLockManager lettuceLockManager;

    @Around("@annotation(kr.hhplus.be.server.config.redis.DistributedLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        String key = REDISSON_LOCK_PREFIX + LockKeyParser.getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), distributedLock.key());
        LockMethod lockMethod = distributedLock.method();
        log.info("lockMethod : {}, key : {}", lockMethod, key);

        switch (lockMethod) {
            case SIMPLE -> {
                if (!lettuceLockManager.lock(key)) {
                    throw new IllegalStateException("RESOURCE LOCKED : 자원이 현재 점유되고 있어 요청에 실패했습니다.");
                }
                try {
                    return joinPoint.proceed();
                } finally {
                    log.info("lettuceLockManager unlock (serviceName : {}, key : {})", method.getName(), key);
                    try {
                        lettuceLockManager.unlock(key);
                    } catch (IllegalMonitorStateException e) {
                        log.info("Lettuce Lock Already UnLock (serviceName : {}, key : {})", method.getName(), key);
                    }
                }
            }
            case SPIN -> {
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
                    return joinPoint.proceed();
                } finally {
                    log.info("SPIN LOCK UNLOCK (serviceName : {}, key : {})", method.getName(), key);
                    try {
                    lettuceLockManager.unlock(key);
                    } catch (IllegalMonitorStateException e) {
                        log.info("Lettuce Lock Already UnLock (serviceName : {}, key : {})", method.getName(), key);
                    }
                }
            }
            case PUBSUB -> {
                RLock rLock = redissonClient.getLock(key);
                try {
                    boolean available = rLock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit());  // (2)
                    if (!available) {
                        throw new IllegalStateException("RESOURCE LOCKED : 자원이 현재 점유되고 있어 요청에 실패했습니다.");
                    }
                    return joinPoint.proceed();

                } catch (InterruptedException e) {
                    throw new InterruptedException();
                } finally {
                    log.info("Redisson Lock UNLOCK (serviceName : {}, key : {})", method.getName(), key);
                    try {
                        rLock.unlock();
                    } catch (IllegalMonitorStateException e) {
                        log.info("Redisson Lock Already UnLock (serviceName : {}, key : {})", method.getName(), key);
                    }
                }
            }
            default -> {
                throw new IllegalArgumentException("LOCK METHOD NOT FOUND : 지원하지 않는 락 방식입니다.");
            }
        }
    }
}
