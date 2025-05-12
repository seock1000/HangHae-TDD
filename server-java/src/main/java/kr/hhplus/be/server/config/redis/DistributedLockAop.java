package kr.hhplus.be.server.config.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
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
    private final LockTemplate lockTemplate;
    private final TransactionHandler transactionHandler;

    @Around("@annotation(kr.hhplus.be.server.config.redis.DistributedLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        String key = LockKeyParser.getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), distributedLock.key()).toString();
        LockMethod lockMethod = distributedLock.method();
        log.info("lockMethod : {}, key : {}", lockMethod, key);

        switch (lockMethod) {
            case SIMPLE -> {
                return lockTemplate.simpleLock(key, () -> {
                    try {
//                        return joinPoint.proceed(); // 트랜잭션 이전 락이 해제될거임 -> 별도의 트랜잭션(REQUEST_NEW)로 실행 후 락 해제 필요
                        return transactionHandler.proceed(joinPoint);
                    } catch (Throwable e) {
                        log.error("SIMPLE lock 실행 중 예외 발생", e);
                        throw new RuntimeException("SIMPLE lock 실행 중 예외가 발생했습니다.", e);
                    }
                });
            }
            case SPIN -> {
                return lockTemplate.spinLock(key, () -> {
                    try {
//                        return joinPoint.proceed(); // 트랜잭션 이전 락이 해제될거임 -> 별도의 트랜잭션(REQUEST_NEW)로 실행 후 락 해제 필요
                        return transactionHandler.proceed(joinPoint);
                    } catch (Throwable e) {
                        log.error("SPIN lock 실행 중 예외 발생", e);
                        throw new RuntimeException("SPIN lock 실행 중 예외가 발생했습니다.", e);
                    }
                });
            }
            case PUBSUB -> {
                return lockTemplate.pubSubLock(key, distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit(), () -> {
                    try {
//                        return joinPoint.proceed(); // 트랜잭션 이전 락이 해제될거임 -> 별도의 트랜잭션(REQUEST_NEW)로 실행 후 락 해제 필요
                        return transactionHandler.proceed(joinPoint);
                    } catch (Throwable e) {
                        log.error("PUBSUB lock 실행 중 예외 발생", e);
                        throw new RuntimeException("PUBSUB lock 실행 중 예외가 발생했습니다.", e);
                    }
                });
            }
            default -> throw new IllegalArgumentException("LOCK METHOD NOT FOUND : 지원하지 않는 락 방식입니다.");
        }
    }
}