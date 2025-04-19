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
import java.util.concurrent.CompletableFuture;

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

        ChargePointCommand command1 = new ChargePointCommand(user.getId(), 1000);
        ChargePointCommand command2 = new ChargePointCommand(user.getId(), 1000);
        ChargePointCommand command3 = new ChargePointCommand(user.getId(), 1000);
        ChargePointCommand command4 = new ChargePointCommand(user.getId(), 1000);

        // When
        CompletableFuture <Void> thread1 = CompletableFuture.runAsync(() -> {
            try {
                pointFacade.charge(command1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        CompletableFuture <Void> thread2 = CompletableFuture.runAsync(() -> {
            try {
                pointFacade.charge(command2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        CompletableFuture <Void> thread3 = CompletableFuture.runAsync(() -> {
            try {
                pointFacade.charge(command3);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        CompletableFuture <Void> thread4 = CompletableFuture.runAsync(() -> {
            try {
                pointFacade.charge(command4);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        CompletableFuture.allOf(thread1, thread2, thread3, thread4).join();

        // Then
        Point point = pointJpaRepository.findByUserId(1L).orElseThrow();
        assertEquals(4000, point.getBalance());
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

        ChargePointCommand pointCommand1 = new ChargePointCommand(user.getId(), 1_000);
        ChargePointCommand pointCommand2 = new ChargePointCommand(user.getId(), 1_000);
        PayCommand payCommand1 = new PayCommand(orderId1);
        PayCommand payCommand2 = new PayCommand(orderId2);

        // When
        CompletableFuture <Void> thread1 = CompletableFuture.runAsync(() -> {
            try {
                pointFacade.charge(pointCommand1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        CompletableFuture <Void> thread2 = CompletableFuture.runAsync(() -> {
            try {
                pointFacade.charge(pointCommand2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        CompletableFuture <Void> thread3 = CompletableFuture.runAsync(() -> {
            try {
                paymentFacade.pay(payCommand1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        CompletableFuture <Void> thread4 = CompletableFuture.runAsync(() -> {
            try {
                paymentFacade.pay(payCommand2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        CompletableFuture.allOf(thread1, thread2, thread3, thread4).join();

        // Then
        Point point = pointJpaRepository.findByUserId(1L).orElseThrow();
        assertEquals(10_000, point.getBalance());
    }
}