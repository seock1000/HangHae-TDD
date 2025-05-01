package kr.hhplus.be.server.config.redis;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.lang.reflect.Method;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DistributedLockAopTest {

    @Mock
    RedissonClient redissonClient;
    @Mock
    RLock rLock;
    @Mock
    LettuceLockManager lettuceLockManager;
    @InjectMocks
    DistributedLockAop distributedLockAop;

    @Test
    @DisplayName("lock 메소드로 SIMPLE이 전달되면 Lettuce lock, unlock이 호출되어야 한다.")
    void SIMPLE_방식으로_락을_획득하고_해제한다() throws Throwable {
        // given
        String param = "test";
        String key = "test:" + param;
        String resolvedKey = "LOCK:" + key;

        Method method = DummyService.class.getMethod("dummyMethodSimple", String.class);

        MethodSignature methodSignature = mock(MethodSignature.class);
        given(methodSignature.getMethod()).willReturn(method);
        given(methodSignature.getParameterNames()).willReturn(new String[]{"param"});

        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        given(joinPoint.getSignature()).willReturn(methodSignature);
        given(joinPoint.getArgs()).willReturn(new Object[]{param});

        given(lettuceLockManager.lock(resolvedKey)).willReturn(true);

        // when
        distributedLockAop.lock(joinPoint);

        // then
        verify(lettuceLockManager, times(1)).lock(resolvedKey);
        verify(joinPoint).proceed();
        verify(lettuceLockManager, times(1)).unlock(resolvedKey);
    }

    @Test
    @DisplayName("lock 메소드로 SIMPLE이 전달되면 Lettuce lock, unlock이 호출되어야 한다.")
    void SPIN_방식으로_락을_획득하고_해제한다() throws Throwable {
        // given
        String param = "test";
        String key = "test:" + param;
        String resolvedKey = "LOCK:" + key;

        Method method = DummyService.class.getMethod("dummyMethodSimple", String.class);

        MethodSignature methodSignature = mock(MethodSignature.class);
        given(methodSignature.getMethod()).willReturn(method);
        given(methodSignature.getParameterNames()).willReturn(new String[]{"param"});

        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        given(joinPoint.getSignature()).willReturn(methodSignature);
        given(joinPoint.getArgs()).willReturn(new Object[]{param});

        given(lettuceLockManager.lock(resolvedKey)).willReturn(true);

        // when
        distributedLockAop.lock(joinPoint);

        // then
        verify(lettuceLockManager, times(1)).lock(resolvedKey);
        verify(joinPoint).proceed();
        verify(lettuceLockManager, times(1)).unlock(resolvedKey);
    }

    @Test
    @DisplayName("lock 메소드로 PUBSUB이 전달되면 Redisson getLock, tryLock, unlock이 호출되어야 한다.")
    void PUBSUB_방식으로_락을_획득하고_해제한다() throws Throwable {
        // given
        String param = "test";
        String key = "test:" + param;
        String resolvedKey = "LOCK:" + key;

        // 가짜 서비스 메서드
        Method method = DummyService.class.getMethod("dummyMethodPubSub", String.class);

        MethodSignature methodSignature = mock(MethodSignature.class);
        given(methodSignature.getMethod()).willReturn(method);
        given(methodSignature.getParameterNames()).willReturn(new String[]{"param"});

        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        given(joinPoint.getSignature()).willReturn(methodSignature);
        given(joinPoint.getArgs()).willReturn(new Object[]{param});

        given(redissonClient.getLock(resolvedKey)).willReturn(rLock);
        given(rLock.tryLock(anyLong(), anyLong(), any())).willReturn(true);
        given(joinPoint.proceed()).willReturn("SUCCESS");

        // when
        distributedLockAop.lock(joinPoint);

        // then
        verify(redissonClient, times(1)).getLock(resolvedKey);
        verify(rLock, times(1)).tryLock(anyLong(), anyLong(), any());
        verify(joinPoint).proceed();
        verify(rLock, times(1)).unlock();
    }

    // 테스트용 더미 서비스
    static class DummyService {

        @DistributedLock(key = "'test:' + #param", method = LockMethod.SIMPLE)
        public String dummyMethodSimple(String param) {
            return "SUCCESS";
        }

        @DistributedLock(key = "'test:' + #param", method = LockMethod.SPIN)
        public String dummyMethodSpin(String param) {
            return "SUCCESS";
        }

        @DistributedLock(key = "'test:' + #param", method = LockMethod.PUBSUB)
        public String dummyMethodPubSub(String param) {
            return "SUCCESS";
        }
    }
}