package kr.hhplus.be.server.config.redis;

import kr.hhplus.be.server.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
public class DistributedLockAopIntegrationTest {

    @MockitoSpyBean
    LockTemplate lockTemplate;

    @Autowired
    private DummyService dummyService = new DummyService();

    @Test
    @DisplayName("lock 메소드로 SIMPLE이 전달되면 LockTemplate simpleLock이 호출되어야 한다.")
    void SIMPLE_방식으로_락을_획득하고_해제한다() throws Throwable {
        // given
        String param = "test";
        String key = "test:" + param;

        // when
        dummyService.dummyMethodSimple(param);

        // then
        verify(lockTemplate, times(1)).simpleLock(eq(key), any());
    }

    @Test
    @DisplayName("lock 메소드로 SPIN이 전달되면 LockTemplate spinLock이 호출되어야 한다.")
    void SPIN_방식으로_락을_획득하고_해제한다() throws Throwable {
        // given
        String param = "test";
        String key = "test:" + param;

        // when
        dummyService.dummyMethodSpin(param);

        // then
        verify(lockTemplate, times(1)).spinLock(eq(key), any());
    }

    @Test
    @DisplayName("lock 메소드로 PUBSUB이 전달되면 Redisson getLock, tryLock, unlock이 호출되어야 한다.")
    void PUBSUB_방식으로_락을_획득하고_해제한다() throws Throwable {
        // given
        String param = "test";
        String key = "test:" + param;

        // when
        dummyService.dummyMethodPubSub(param);

        // then
        verify(lockTemplate, times(1)).pubSubLock(eq(key), anyLong(), anyLong(), any(), any());
    }

    @Test
    @DisplayName("simple lock 방식의 동시 요청은 한 건을 제외하고 모두 실패해야 한다.")
    void SIMPLE_LOCK_동시_요청은_락을_획득한_한건만_성공한다() {
        // given
        String param = "test";
        int threadCount = 10;
        AtomicInteger successCount = new AtomicInteger(0);
        int expectedSuccessCount = 1;

        // when
        List<CompletableFuture<Void>> threads = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            threads.add(CompletableFuture.runAsync(() -> {
                try {
                    String result = dummyService.dummyMethodSimple(param);
                    if ("SUCCESS".equals(result)) {
                        successCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));
        }

        CompletableFuture.allOf(threads.toArray(new CompletableFuture[0])).join();

        // then
        assertEquals(expectedSuccessCount, successCount.get());
    }

    @Test
    @DisplayName("spin lock 방식의 동시 요청은 모두 성공해야 한다.")
    void SPIN_LOCK_동시_요청은_모두_성공한다() {
        // given
        String param = "test";
        int threadCount = 10;
        AtomicInteger successCount = new AtomicInteger(0);
        int expectedSuccessCount = 10;

        // when
        List<CompletableFuture<Void>> threads = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            threads.add(CompletableFuture.runAsync(() -> {
                try {
                    String result = dummyService.dummyMethodSpin(param);
                    if ("SUCCESS".equals(result)) {
                        successCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));
        }

        CompletableFuture.allOf(threads.toArray(new CompletableFuture[0])).join();

        // then
        assertEquals(expectedSuccessCount, successCount.get());
    }

    @Test
    @DisplayName("pubsub lock 방식의 동시 요청은 모두 성공해야 한다.")
    void PUBSUB_LOCK_동시_요청은_모두_성공한다() {
        // given
        String param = "test";
        int threadCount = 10;
        AtomicInteger successCount = new AtomicInteger(0);
        int expectedSuccessCount = 10;

        // when
        List<CompletableFuture<Void>> threads = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            threads.add(CompletableFuture.runAsync(() -> {
                try {
                    String result = dummyService.dummyMethodPubSub(param);
                    if ("SUCCESS".equals(result)) {
                        successCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));
        }

        CompletableFuture.allOf(threads.toArray(new CompletableFuture[0])).join();

        // then
        assertEquals(expectedSuccessCount, successCount.get());
    }

    @TestConfiguration
    static class DummyConfig {
        @Bean
        public DummyService dummyService() {
            return new DummyService();
        }
    }

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
