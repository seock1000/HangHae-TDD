package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.application.point.PointFacade;
import kr.hhplus.be.server.config.redis.LockTemplate;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.order.Orders;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
class PaymentLockFacadeConcurrencyTest {

    @MockitoBean
    private PaymentFacade paymentFacade;
    @MockitoBean
    private OrderService orderService;
    @MockitoSpyBean
    private LockTemplate lockTemplate;
    @Autowired
    private PaymentLockFacade paymentLockFacade;

    @Test
    @DisplayName("같은 주문에 대한 결제 요청은 순차적으로 실행된다.")
    void 같은_주문에_대한_결제_요청은_순차적으로_실행된다() {
        // given
        PayCommand command = Instancio.of(PayCommand.class)
                .set(field("orderId"), "orderId")
                .create();
        int threadCount = 5;
        Mockito.when(orderService.getOrderById(anyString()))
                .thenReturn(Instancio.create(Orders.class));

        // 0.1초 대기 후 결제 요청을 처리
        List<Pair<Long, Long>> startAndEnds = new ArrayList<>();
        Mockito.when(paymentFacade.pay(any()))
                .thenAnswer(invocation -> {
                    long start = System.currentTimeMillis();
                    Thread.sleep(100);
                    long end = System.currentTimeMillis();
                    startAndEnds.add(Pair.of(start, end));
                    return null;
                });

        // when
        List<CompletableFuture<Void>> threads = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            threads.add(CompletableFuture.runAsync(() -> {
                paymentLockFacade.pay(command);
            }));
        }
        CompletableFuture.allOf(threads.toArray(new CompletableFuture[0])).join();

        // then
        verify(paymentFacade, times(threadCount)).pay(any());
        verify(lockTemplate, times(threadCount)).executeWithLocks(any(), any());
        for (int i = 0; i < startAndEnds.size() - 1; i++) {
            long curEnd = startAndEnds.get(i).getSecond();
            long nextStart = startAndEnds.get(i + 1).getFirst();
            assertTrue(curEnd < nextStart);
        }
    }

    @Test
    @DisplayName("다른 주문에 대한 결제 요청은 동시에 실행된다.")
    void 다른_주문에_대한_결제_요청은_동시에_실행된다() {
        // given
        String orderIdPrefix = "orderId";
        List<PayCommand> commands = new ArrayList<>();
        int threadCount = 5;
        for(int i = 0; i < threadCount; i++) {
            String orderId = orderIdPrefix + i;
            commands.add(Instancio.of(PayCommand.class)
                    .set(field("orderId"), orderId)
                    .create());
            Mockito.when(orderService.getOrderById(orderId))
                    .thenReturn(Instancio.of(Orders.class)
                            .set(field("user"), Integer.toUnsignedLong(i))
                            .create());
        }

        // 1.5초 대기 후 결제 요청을 처리
        Mockito.when(paymentFacade.pay(any()))
                .thenAnswer(invocation -> {
                    Thread.sleep(1500);
                    return null;
                });

        // when
        List<CompletableFuture<Void>> threads = commands.stream()
                .map(command -> CompletableFuture.runAsync(() -> {
                    paymentLockFacade.pay(command);
                }))
                .toList();
        CompletableFuture.allOf(threads.toArray(new CompletableFuture[0])).join();

        // then
        verify(paymentFacade, times(threadCount)).pay(any());
        verify(lockTemplate, times(threadCount)).executeWithLocks(any(), any());
    }

    @Test
    @DisplayName("같은 포인트(유저)에 대한 결제 요청은 순차적으로 실행된다.")
    void 같은_포인트에_대한_결제_요청은_순차적으로_실행된다() {
        // given
        PayCommand command = Instancio.of(PayCommand.class)
                .set(field("orderId"), "orderId")
                .create();
        int threadCount = 5;
        Mockito.when(orderService.getOrderById(anyString()))
                .thenReturn(Instancio.of(Orders.class)
                        .set(field("user"), 1L)
                        .create());

        // 0.1초 대기 후 결제 요청을 처리
        List<Pair<Long, Long>> startAndEnds = new ArrayList<>();
        Mockito.when(paymentFacade.pay(any()))
                .thenAnswer(invocation -> {
                    long start = System.currentTimeMillis();
                    Thread.sleep(100);
                    long end = System.currentTimeMillis();
                    startAndEnds.add(Pair.of(start, end));
                    return null;
                });

        // when
        List<CompletableFuture<Void>> threads = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            threads.add(CompletableFuture.runAsync(() -> {
                paymentLockFacade.pay(command);
            }));
        }
        CompletableFuture.allOf(threads.toArray(new CompletableFuture[0])).join();

        // then
        verify(paymentFacade, times(threadCount)).pay(any());
        verify(lockTemplate, times(threadCount)).executeWithLocks(any(), any());
        for (int i = 0; i < startAndEnds.size() - 1; i++) {
            long curEnd = startAndEnds.get(i).getSecond();
            long nextStart = startAndEnds.get(i + 1).getFirst();
            assertTrue(curEnd < nextStart);
        }
    }

}