package kr.hhplus.be.server.application.point;

import kr.hhplus.be.server.application.order.PlaceOrderCommand;
import kr.hhplus.be.server.application.payment.PayCommand;
import kr.hhplus.be.server.application.payment.PaymentFacade;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.order.Orders;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointService;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.infrastructure.order.OrderJpaRepository;
import kr.hhplus.be.server.infrastructure.point.PointJpaRepository;
import kr.hhplus.be.server.infrastructure.user.UserJpaRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PointFacadeConcurrencyTest {

    @Autowired
    private PointFacade pointFacade;
    @Autowired
    private PointJpaRepository pointJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private OrderJpaRepository orderJpaRepository;
    @Autowired
    private PaymentFacade paymentFacade;

    @AfterEach
    void tearDown() {
        pointJpaRepository.deleteAll();
        userJpaRepository.deleteAll();
        orderJpaRepository.deleteAll();
    }

    @Test
    @DisplayName("여러 스레드에서 동시에 포인트 충전 요청 시 정확하게 합산된 값이 반영되어야 한다.")
    void testConcurrentCharge() throws InterruptedException {
        // Given
        User user = userJpaRepository.saveAndFlush(Instancio.of(User.class)
                .set(field("id"), null)
                .create());

        pointJpaRepository.saveAndFlush(Instancio.of(Point.class)
                        .set(field("id"), null)
                        .set(field("userId"), user.getId())
                        .set(field("balance"), 0)
                        .set(field("histories"), new ArrayList<>())
                .create());

        var commands = List.of(
                new ChargePointCommand(user.getId(), 1_000),
                new ChargePointCommand(user.getId(), 1_000),
                new ChargePointCommand(user.getId(), 1_000),
                new ChargePointCommand(user.getId(), 1_000)
        );
        AtomicInteger counter = new AtomicInteger(0);

        // When
        List<CompletableFuture<Void>> threads = new ArrayList<>();
        for(var command : commands) {
            threads.add(CompletableFuture.runAsync(() -> {
                try {
                    pointFacade.charge(command);
                    counter.incrementAndGet();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));
        }
        CompletableFuture.allOf(threads.toArray(new CompletableFuture[0])).join();

        // Then
        Point point = pointJpaRepository.findByUserId(user.getId()).orElseThrow();
        int expected = 0 + (counter.get() * 1_000);
        assertEquals(expected, point.getBalance());
    }


    @Test
    @DisplayName("여러 스레드에서 포인트 충전과 결제 요청이 동시에 이루어질 때 정확하게 합산된 값이 반영되어야 한다.")
    void testConcurrentChargeAndPayment() {
        // Given
        User user = userJpaRepository.saveAndFlush(Instancio.of(User.class)
                .set(field("id"), null)
                .create());

        pointJpaRepository.saveAndFlush(Instancio.of(Point.class)
                        .set(field("id"), null)
                        .set(field("userId"), user.getId())
                        .set(field("balance"), 10_000)
                        .set(field("histories"), new ArrayList<>())
                .create());

        String orderId1 = "orderId1";
        String orderId2 = "orderId2";
        orderJpaRepository.saveAndFlush(Instancio.of(Orders.class)
                .set(field("id"), orderId1)
                .set(field("user"), user.getId())
                .set(field("totalAmount"), 1_000)
                .set(field("couponId"), null)
                .set(field("status"), OrderStatus.PENDING)
                .set(field("orderItems"), new ArrayList<>())
                .create());
        orderJpaRepository.saveAndFlush(Instancio.of(Orders.class)
                .set(field("id"), orderId2)
                .set(field("user"), user.getId())
                .set(field("totalAmount"), 1_000)
                .set(field("couponId"), null)
                .set(field("status"), OrderStatus.PENDING)
                .set(field("orderItems"), new ArrayList<>())
                .create());

        var chargeCommands = List.of(
                new ChargePointCommand(user.getId(), 1_000),
                new ChargePointCommand(user.getId(), 1_000)
        );
        var payCommands = List.of(
                new PayCommand(orderId1),
                new PayCommand(orderId2)
        );
        AtomicInteger chargeCounter = new AtomicInteger(0);
        AtomicInteger paymentCounter = new AtomicInteger(0);

        // When
        List<CompletableFuture<Void>> threads = new ArrayList<>();
        for(var command : chargeCommands) {
            threads.add(CompletableFuture.runAsync(() -> {
                try {
                    pointFacade.charge(command);
                    chargeCounter.incrementAndGet();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));
        }
        for(var command : payCommands) {
            threads.add(CompletableFuture.runAsync(() -> {
                try {
                    paymentFacade.pay(command);
                    paymentCounter.incrementAndGet();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));
        }

        CompletableFuture.allOf(threads.toArray(new CompletableFuture[0])).join();

        // Then
        Point point = pointJpaRepository.findByUserId(user.getId()).orElseThrow();
        int expected = 10_000 + (chargeCounter.get() * 1_000) - (paymentCounter.get() * 1_000);
        assertEquals(expected, point.getBalance());
    }

    @Test
    @DisplayName("여러 스레드에서 동시에 결제 요청 시 정확하게 차감된 값이 반영되어야 한다.")
    void testConcurrentPayment() {
        // Given
        User user = userJpaRepository.saveAndFlush(Instancio.of(User.class)
                .set(field("id"), null)
                .create());

        pointJpaRepository.saveAndFlush(Instancio.of(Point.class)
                        .set(field("id"), null)
                        .set(field("userId"), user.getId())
                        .set(field("balance"), 10_000)
                        .set(field("histories"), new ArrayList<>())
                .create());

        String orderId1 = "orderId1";
        String orderId2 = "orderId2";
        String orderId3 = "orderId3";
        String orderId4 = "orderId4";
        orderJpaRepository.saveAndFlush(Instancio.of(Orders.class)
                .set(field("id"), orderId1)
                .set(field("user"), user.getId())
                .set(field("totalAmount"), 1_000)
                .set(field("couponId"), null)
                .set(field("status"), OrderStatus.PENDING)
                .set(field("orderItems"), new ArrayList<>())
                .create());
        orderJpaRepository.saveAndFlush(Instancio.of(Orders.class)
                .set(field("id"), orderId2)
                .set(field("user"), user.getId())
                .set(field("totalAmount"), 1_000)
                .set(field("couponId"), null)
                .set(field("status"), OrderStatus.PENDING)
                .set(field("orderItems"), new ArrayList<>())
                .create());
        orderJpaRepository.saveAndFlush(Instancio.of(Orders.class)
                .set(field("id"), orderId3)
                .set(field("user"), user.getId())
                .set(field("totalAmount"), 1_000)
                .set(field("couponId"), null)
                .set(field("status"), OrderStatus.PENDING)
                .set(field("orderItems"), new ArrayList<>())
                .create());
        orderJpaRepository.saveAndFlush(Instancio.of(Orders.class)
                .set(field("id"), orderId4)
                .set(field("user"), user.getId())
                .set(field("totalAmount"), 1_000)
                .set(field("couponId"), null)
                .set(field("status"), OrderStatus.PENDING)
                .set(field("orderItems"), new ArrayList<>())
                .create());

        var commands = List.of(
                new PayCommand(orderId1),
                new PayCommand(orderId2),
                new PayCommand(orderId3),
                new PayCommand(orderId4)
        );
        AtomicInteger counter = new AtomicInteger(0);

        // When
        List<CompletableFuture<Void>> threads = new ArrayList<>();

        for(var command : commands) {
            threads.add(CompletableFuture.runAsync(() -> {
                try {
                    paymentFacade.pay(command);
                    counter.incrementAndGet();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));
        }

        CompletableFuture.allOf(threads.toArray(new CompletableFuture[0])).join();

        // Then
        Point point = pointJpaRepository.findByUserId(user.getId()).orElseThrow();
        int expected = 10_000 - (counter.get() * 1_000);
        assertEquals(expected, point.getBalance());
    }
}