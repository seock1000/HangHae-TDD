package kr.hhplus.be.server.application.order;

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
import java.util.concurrent.atomic.AtomicInteger;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
class OrderLockFacadeConcurrencyTest {

    @MockitoBean
    private OrderFacade orderFacade;
    @MockitoBean
    private OrderService orderService;
    @MockitoSpyBean
    private LockTemplate lockTemplate;
    @Autowired
    private OrderLockFacade orderLockFacade;

    @Test
    @DisplayName("같은 쿠폰에 대한 주문 요청은 락획득 순서에 따라 순차적으로 실행된다.")
    void 같은_쿠폰에_대한_주문_요청은_락획득_순서에_따라_순차적으로_실행된다() {
        // given
        Long couponId = 1L;
        List<PlaceOrderCommand> commands = List.of(
                new PlaceOrderCommand(1L, couponId, List.of(new PlaceOrderCommand.PlaceOrderItem(100L, 1))),
                new PlaceOrderCommand(1L, couponId, List.of(new PlaceOrderCommand.PlaceOrderItem(101L, 1))),
                new PlaceOrderCommand(1L, couponId, List.of(new PlaceOrderCommand.PlaceOrderItem(102L, 1))),
                new PlaceOrderCommand(1L, couponId, List.of(new PlaceOrderCommand.PlaceOrderItem(103L, 1))),
                new PlaceOrderCommand(1L, couponId, List.of(new PlaceOrderCommand.PlaceOrderItem(104L, 1)))
                );

        // 성공 시 0.1초 대기 후 응답
        List<Pair<Long, Long>> startAndEnds = new ArrayList<>();
        Mockito.when(orderFacade.placeOrder(any()))
                .thenAnswer(invocation -> {
                    long start = System.currentTimeMillis();
                    Thread.sleep(100); // 락 점유 시간 확보
                    long end = System.currentTimeMillis();
                    startAndEnds.add(Pair.of(start, end));
                    return Instancio.create(OrderResult.class);
                });

        // when
        List<CompletableFuture<Void>> threads = commands.stream()
                .map(command -> CompletableFuture.runAsync(() -> {
                    try {
                        orderLockFacade.placeOrder(command);
                    } catch (Exception e) {
                        // 예외 처리
                    }
                }))
                .toList();

        CompletableFuture.allOf(threads.toArray(new CompletableFuture[0])).join();

        // then
        verify(lockTemplate, times(commands.size())).executeWithLocks(any(), any());
        verify(orderFacade, times(commands.size())).placeOrder(any(PlaceOrderCommand.class));
        for(int i = 0; i < startAndEnds.size() - 1; i++) {
            long curEnd = startAndEnds.get(i).getSecond();
            long nextStart = startAndEnds.get(i + 1).getFirst();
            assertTrue(curEnd < nextStart);
        }
    }

    @Test
    @DisplayName("서로 다른 쿠폰에 대한 주문 요청은 서로 Block 되지 않는다.")
    void 서로_다른_쿠폰에_대한_주문_요청은_서로_Block_되지_않는다() {
        // given
        List<PlaceOrderCommand> commands = List.of(
                new PlaceOrderCommand(1L, 1L, List.of(new PlaceOrderCommand.PlaceOrderItem(100L, 1))),
                new PlaceOrderCommand(1L, 2L, List.of(new PlaceOrderCommand.PlaceOrderItem(101L, 1))),
                new PlaceOrderCommand(1L, 3L, List.of(new PlaceOrderCommand.PlaceOrderItem(102L, 1))),
                new PlaceOrderCommand(1L, 4L, List.of(new PlaceOrderCommand.PlaceOrderItem(103L, 1))),
                new PlaceOrderCommand(1L, 5L, List.of(new PlaceOrderCommand.PlaceOrderItem(104L, 1)))
        );

        // 성공 시 1.5초 대기 후 응답
        Mockito.when(orderFacade.placeOrder(any()))
                .thenAnswer(invocation -> {
                    Thread.sleep(1500); // 락 점유 시간 확보
                    return Instancio.create(OrderResult.class);
                });

        // when
        List<CompletableFuture<Void>> threads = commands.stream()
                .map(command -> CompletableFuture.runAsync(() -> {
                    try {
                        orderLockFacade.placeOrder(command);
                    } catch (Exception e) {
                        // 예외 처리
                    }
                }))
                .toList();

        CompletableFuture.allOf(threads.toArray(new CompletableFuture[0])).join();

        // then
        verify(lockTemplate, times(commands.size())).executeWithLocks(any(), any());
        verify(orderFacade, times(commands.size())).placeOrder(any(PlaceOrderCommand.class));
    }

    @Test
    @DisplayName("같은 상품에 대한 주문 요청은 락획득 순서에 따라 순차적으로 실행된다.")
    void 같은_상품에_대한_주문_요청은_락획득_순서에_따라_순차적으로_성공한다() {
        // given
        List<PlaceOrderCommand> commands = new ArrayList<>();
        for (long i = 1; i <= 5; i++) {
            commands.add(new PlaceOrderCommand(1L, null, List.of(new PlaceOrderCommand.PlaceOrderItem(1, 1))));
        }

        // 성공 시 0.1초 대기 후 응답
        List<Pair<Long, Long>> startAndEnds = new ArrayList<>();
        Mockito.when(orderFacade.placeOrder(any()))
                .thenAnswer(invocation -> {
                    long start = System.currentTimeMillis();
                    Thread.sleep(100); // 락 점유 시간 확보
                    long end = System.currentTimeMillis();
                    startAndEnds.add(Pair.of(start, end));
                    return Instancio.create(OrderResult.class);
                });

        // when
        List<CompletableFuture<Void>> threads = commands.stream()
                .map(command -> CompletableFuture.runAsync(() -> {
                    try {
                        orderLockFacade.placeOrder(command);
                    } catch (Exception e) {
                        // 예외 처리
                    }
                }))
                .toList();
        CompletableFuture.allOf(threads.toArray(new CompletableFuture[0])).join();

        // then
        verify(lockTemplate, times(commands.size())).executeWithLocks(any(), any());
        verify(orderFacade, times(commands.size())).placeOrder(any(PlaceOrderCommand.class));
        for(int i = 0; i < startAndEnds.size() - 1; i++) {
            long curEnd = startAndEnds.get(i).getSecond();
            long nextStart = startAndEnds.get(i + 1).getFirst();
            assertTrue(curEnd < nextStart);
        }
    }

    @Test
    @DisplayName("서로 다른 상품에 대한 주문 요청은 서로 Block 되지 않는다.")
    void 서로_다른_상품에_대한_주문_요청은_서로_Block_되지_않는다() {
        // given
        List<PlaceOrderCommand> commands = new ArrayList<>();
        for (long i = 1; i <= 5; i++) {
            commands.add(new PlaceOrderCommand(1L, null, List.of(new PlaceOrderCommand.PlaceOrderItem(i, 1))));
        }

        // 성공 시 1.5초 대기 후 응답
        Mockito.when(orderFacade.placeOrder(any()))
                .thenAnswer(invocation -> {
                    Thread.sleep(1500); // 락 점유 시간 확보
                    return Instancio.create(OrderResult.class);
                });

        // when
        List<CompletableFuture<Void>> threads = commands.stream()
                .map(command -> CompletableFuture.runAsync(() -> {
                    try {
                        orderLockFacade.placeOrder(command);
                    } catch (Exception e) {
                        // 예외 처리
                    }
                }))
                .toList();
        CompletableFuture.allOf(threads.toArray(new CompletableFuture[0])).join();

        // then
        verify(lockTemplate, times(commands.size())).executeWithLocks(any(), any());
        verify(orderFacade, times(commands.size())).placeOrder(any(PlaceOrderCommand.class));
    }

    @Test
    @DisplayName("같은 쿠폰에 대한 주문 취소 요청은 락획득 순서에 따라 순차적으로 실행된다.")
    void 같은_쿠폰에_대한_주문_취소_요청은_락획득_순서에_따라_순차적으로_실행된다() {
        // given
        int threadCount = 5;
        Mockito.when(orderService.getOrderById(anyString()))
                .thenReturn(Instancio.of(Orders.class)
                        .set(field("couponId"), 1L)
                        .create());

        // 성공 시 0.1초 대기 후 응답
        List<Pair<Long, Long>> startAndEnds = new ArrayList<>();
        Mockito.when(orderFacade.cancelOrder(any()))
                .thenAnswer(invocation -> {
                    long start = System.currentTimeMillis();
                    Thread.sleep(100); // 락 점유 시간 확보
                    long end = System.currentTimeMillis();
                    startAndEnds.add(Pair.of(start, end));
                    return Instancio.create(OrderResult.class);
                });

        // when
        List<CompletableFuture<Void>> threads = new ArrayList<>();
        for(int i = 0; i < threadCount; i++) {
            threads.add(CompletableFuture.runAsync(() -> {
                try {
                    orderLockFacade.cancelOrder(Instancio.create(CancelOrderCommand.class));
                } catch (Exception e) {
                    // 예외 처리
                }
            }));
        }

        CompletableFuture.allOf(threads.toArray(new CompletableFuture[0])).join();

        // then
        verify(lockTemplate, times(threadCount)).executeWithLocks(any(), any());
        verify(orderFacade, times(threadCount)).cancelOrder(any(CancelOrderCommand.class));
        for(int i = 0; i < startAndEnds.size() - 1; i++) {
            long curEnd = startAndEnds.get(i).getSecond();
            long nextStart = startAndEnds.get(i + 1).getFirst();
            assertTrue(curEnd < nextStart);
        }
    }

    @Test
    @DisplayName("서로 다른 쿠폰에 대한 주문 취소 요청은 서로 Block 되지 않는다.")
    void 서로_다른_쿠폰에_대한_주문_취소_요청은_서로_Block_되지_않는다() {
        // given
        String orderIdPrefix = "orderId";
        int threadCount = 5;
        for(int i = 0; i < threadCount; i++) {
            Mockito.when(orderService.getOrderById(orderIdPrefix + i))
                    .thenReturn(Instancio.of(Orders.class)
                            .set(field("couponId"), (long) i)
                            .create());
        }

        // 성공 시 1.5초 대기 후 응답
        Mockito.when(orderFacade.cancelOrder(any()))
                .thenAnswer(invocation -> {
                    Thread.sleep(1500); // 락 점유 시간 확보
                    return Instancio.create(OrderResult.class);
                });

        // when
        List<CompletableFuture<Void>> threads = new ArrayList<>();
        for(int i = 0; i < threadCount; i++) {
            final int seq = i;
            threads.add(CompletableFuture.runAsync(() -> {
                try {
                    orderLockFacade.cancelOrder(new CancelOrderCommand("orderId" + seq));
                } catch (Exception e) {
                    // 예외 처리
                }
            }));
        }
        CompletableFuture.allOf(threads.toArray(new CompletableFuture[0])).join();

        // then
        verify(lockTemplate, times(threadCount)).executeWithLocks(any(), any());
        verify(orderFacade, times(threadCount)).cancelOrder(any(CancelOrderCommand.class));
    }

    @Test
    @DisplayName("같은 상품에 대한 주문 취소 요청은 락획득 순서에 따라 순차적으로 실행된다.")
    void 같은_상품에_대한_주문_취소_요청은_락획득_순서에_따라_순차적으로_성공한다() {
        // given
        int threadCount = 5;
        Mockito.when(orderService.getOrderById(anyString()))
                .thenReturn(Instancio.of(Orders.class)
                        .set(field("orderItems"), List.of(Instancio.of(OrderItem.class)
                                .set(field("productId"), 1L)
                                .create()))
                        .create());

        // 성공 시 0.1초 대기 후 응답
        List<Pair<Long, Long>> startAndEnds = new ArrayList<>();
        Mockito.when(orderFacade.cancelOrder(any()))
                .thenAnswer(invocation -> {
                    long start = System.currentTimeMillis();
                    Thread.sleep(100); // 락 점유 시간 확보
                    long end = System.currentTimeMillis();
                    startAndEnds.add(Pair.of(start, end));
                    return Instancio.create(OrderResult.class);
                });

        // when
        List<CompletableFuture<Void>> threads = new ArrayList<>();
        for(int i = 0; i < threadCount; i++) {
            threads.add(CompletableFuture.runAsync(() -> {
                try {
                    orderLockFacade.cancelOrder(Instancio.create(CancelOrderCommand.class));
                } catch (Exception e) {
                    // 예외 처리
                }
            }));
        }
        CompletableFuture.allOf(threads.toArray(new CompletableFuture[0])).join();

        // then
        verify(lockTemplate, times(threadCount)).executeWithLocks(any(), any());
        verify(orderFacade, times(threadCount)).cancelOrder(any(CancelOrderCommand.class));
        for(int i = 0; i < startAndEnds.size() - 1; i++) {
            long curEnd = startAndEnds.get(i).getSecond();
            long nextStart = startAndEnds.get(i + 1).getFirst();
            assertTrue(curEnd < nextStart);
        }
    }

    @Test
    @DisplayName("서로 다른 상품에 대한 주문 취소 요청은 서로 Block 되지 않는다.")
    void 서로_다른_상품에_대한_주문_취소_요청은_서로_Block_되지_않는다() {
        // given
        String orderIdPrefix = "orderId";
        int threadCount = 5;
        for(int i = 0; i < threadCount; i++) {
            Mockito.when(orderService.getOrderById(orderIdPrefix + i))
                    .thenReturn(Instancio.of(Orders.class)
                            .set(field("orderItems"), List.of(Instancio.of(OrderItem.class)
                                    .set(field("productId"), (long) i)
                                    .create()))
                            .create());
        }

        // 성공 시 1.5초 대기 후 응답
        Mockito.when(orderFacade.cancelOrder(any()))
                .thenAnswer(invocation -> {
                    Thread.sleep(1500); // 락 점유 시간 확보
                    return Instancio.create(OrderResult.class);
                });

        // when
        List<CompletableFuture<Void>> threads = new ArrayList<>();
        for(int i = 0; i < threadCount; i++) {
            final int seq = i;
            threads.add(CompletableFuture.runAsync(() -> {
                try {
                    orderLockFacade.cancelOrder(new CancelOrderCommand(orderIdPrefix + seq));
                } catch (Exception e) {
                    // 예외 처리
                }
            }));
        }
        CompletableFuture.allOf(threads.toArray(new CompletableFuture[0])).join();

        // then
        verify(lockTemplate, times(threadCount)).executeWithLocks(any(), any());
        verify(orderFacade, times(threadCount)).cancelOrder(any(CancelOrderCommand.class));
    }

    @Test
    @DisplayName("같은 쿠폰에 대한 주문과 주문 취소 요청은 순차적으로 실행된다.")
    void 같은_쿠폰에_대한_주문과_주문_취소_요청은_순차적으로_실행된다() {
        // given
        Long couponId = 1L;
        PlaceOrderCommand placeOrderCommand = new PlaceOrderCommand(1L, couponId, List.of(new PlaceOrderCommand.PlaceOrderItem(100L, 1)));
        CancelOrderCommand cancelOrderCommand = new CancelOrderCommand("orderId");
        Mockito.when(orderService.getOrderById(anyString()))
                .thenReturn(Instancio.of(Orders.class)
                        .set(field("couponId"), couponId)
                        .create());

        // 성공 시 0.1초 대기 후 응답
        List<Pair<Long, Long>> startAndEnds = new ArrayList<>();
        Mockito.when(orderFacade.placeOrder(any()))
                .thenAnswer(invocation -> {
                    long start = System.currentTimeMillis();
                    Thread.sleep(100); // 락 점유 시간 확보
                    long end = System.currentTimeMillis();
                    startAndEnds.add(Pair.of(start, end));
                    return Instancio.create(OrderResult.class);
                });
        Mockito.when(orderFacade.cancelOrder(any()))
                .thenAnswer(invocation -> {
                    long start = System.currentTimeMillis();
                    Thread.sleep(100); // 락 점유 시간 확보
                    long end = System.currentTimeMillis();
                    startAndEnds.add(Pair.of(start, end));
                    return Instancio.create(OrderResult.class);
                });

        // when
        CompletableFuture<Void> placeOrderThread = CompletableFuture.runAsync(() -> {
            try {
                orderLockFacade.placeOrder(placeOrderCommand);
            } catch (Exception e) {
                // 예외 처리
            }
        });

        CompletableFuture<Void> cancelOrderThread = CompletableFuture.runAsync(() -> {
            try {
                orderLockFacade.cancelOrder(cancelOrderCommand);
            } catch (Exception e) {
                // 예외 처리
            }
        });

        CompletableFuture.allOf(placeOrderThread, cancelOrderThread).join();

        // then
        verify(lockTemplate, times(2)).executeWithLocks(any(), any());
        verify(orderFacade, times(1)).placeOrder(any(PlaceOrderCommand.class));
        verify(orderFacade, times(1)).cancelOrder(any(CancelOrderCommand.class));

        for (int i = 0; i < startAndEnds.size() - 1; i++) {
            long curEnd = startAndEnds.get(i).getSecond();
            long nextStart = startAndEnds.get(i + 1).getFirst();
            assertTrue(curEnd < nextStart);
        }
    }

    @Test
    @DisplayName("같은 상품에 대한 주문과 주문 취소 요청은 순차적으로 실행된다.")
    void 같은_상품에_대한_주문과_주문취소_요청은_순차적으로_실행된다() {
        // given
        List<PlaceOrderCommand> commands = new ArrayList<>();
        for (long i = 1; i <= 3; i++) {
            commands.add(new PlaceOrderCommand(1L, null, List.of(new PlaceOrderCommand.PlaceOrderItem(1, 1))));
        }
        Mockito.when(orderService.getOrderById(anyString()))
                .thenReturn(Instancio.of(Orders.class)
                        .set(field("orderItems"), List.of(Instancio.of(OrderItem.class)
                                .set(field("productId"), 1L)
                                .create()))
                        .create());

        // 성공 시 0.1초 대기 후 응답
        List<Pair<Long, Long>> startAndEnds = new ArrayList<>();
        Mockito.when(orderFacade.placeOrder(any()))
                .thenAnswer(invocation -> {
                    long start = System.currentTimeMillis();
                    Thread.sleep(100); // 락 점유 시간 확보
                    long end = System.currentTimeMillis();
                    startAndEnds.add(Pair.of(start, end));
                    return Instancio.create(OrderResult.class);
                });
        Mockito.when(orderFacade.cancelOrder(any()))
                .thenAnswer(invocation -> {
                    long start = System.currentTimeMillis();
                    Thread.sleep(100); // 락 점유 시간 확보
                    long end = System.currentTimeMillis();
                    startAndEnds.add(Pair.of(start, end));
                    return Instancio.create(OrderResult.class);
                });

        // when
        List<CompletableFuture<Void>> threads = new ArrayList<>();
        commands.forEach(command -> {
            threads.add(CompletableFuture.runAsync(() -> {
                try {
                    orderLockFacade.placeOrder(command);
                } catch (Exception e) {
                    // 예외 처리
                }
            }));
            threads.add(CompletableFuture.runAsync(() -> {
                try {
                    orderLockFacade.cancelOrder(Instancio.create(CancelOrderCommand.class));
                } catch (Exception e) {
                    // 예외 처리
                }
            }));
        });
        CompletableFuture.allOf(threads.toArray(new CompletableFuture[0])).join();

        // then
        verify(lockTemplate, times(commands.size() * 2)).executeWithLocks(any(), any());
        verify(orderFacade, times(commands.size())).placeOrder(any(PlaceOrderCommand.class));
        verify(orderFacade, times(commands.size())).cancelOrder(any(CancelOrderCommand.class));
        for (int i = 0; i < startAndEnds.size() - 1; i++) {
            long curEnd = startAndEnds.get(i).getSecond();
            long nextStart = startAndEnds.get(i + 1).getFirst();
            assertTrue(curEnd < nextStart);
        }
    }

}
