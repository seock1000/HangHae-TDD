package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.DiscountType;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.order.Orders;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.infrastructure.coupon.CouponJpaRepository;
import kr.hhplus.be.server.infrastructure.coupon.UserCouponJpaRepository;
import kr.hhplus.be.server.infrastructure.order.OrderJpaRepository;
import kr.hhplus.be.server.infrastructure.product.ProductJpaRepository;
import kr.hhplus.be.server.infrastructure.user.UserJpaRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderFacadeConcurrentTest {

    @Autowired
    private OrderFacade orderFacade;
    @Autowired
    private OrderJpaRepository orderJpaRepository;
    @Autowired
    private CouponJpaRepository couponJpaRepository;
    @Autowired
    private UserCouponJpaRepository userCouponJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private ProductJpaRepository productJpaRepository;

    @BeforeEach
    void tearDown() {
        orderJpaRepository.deleteAll();
        couponJpaRepository.deleteAll();
        userCouponJpaRepository.deleteAll();
        userJpaRepository.deleteAll();
        productJpaRepository.deleteAll();
    }

    @Test
    @DisplayName("여러 스레드에서 동시에 상품 주문 요청 시, 상품 재고 관리가 정확하게 되어야 한다.")
    void testConcurrentOrder() {
        // Given
        var user = userJpaRepository.saveAndFlush(Instancio.of(User.class)
                .set(field("id"), null)
                .create());

        var product = productJpaRepository.saveAndFlush(Instancio.of(Product.class)
                .set(field("id"), null)
                .set(field("stock"), 100)
                .create());

        PlaceOrderCommand command1 = new PlaceOrderCommand(user.getId(), null,
                List.of(new PlaceOrderCommand.PlaceOrderItem(product.getId(), 10)));
        PlaceOrderCommand command2 = new PlaceOrderCommand(user.getId(), null,
                List.of(new PlaceOrderCommand.PlaceOrderItem(product.getId(), 10)));
        PlaceOrderCommand command3 = new PlaceOrderCommand(user.getId(), null,
                List.of(new PlaceOrderCommand.PlaceOrderItem(product.getId(), 10)));
        PlaceOrderCommand command4 = new PlaceOrderCommand(user.getId(), null,
                List.of(new PlaceOrderCommand.PlaceOrderItem(product.getId(), 10)));

        // When
        CompletableFuture<Void> thread1 = CompletableFuture.runAsync(() -> {
            try {
                orderFacade.placeOrder(command1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        CompletableFuture<Void> thread2 = CompletableFuture.runAsync(() -> {
            try {
                orderFacade.placeOrder(command2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        CompletableFuture<Void> thread3 = CompletableFuture.runAsync(() -> {
            try {
                orderFacade.placeOrder(command3);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        CompletableFuture<Void> thread4 = CompletableFuture.runAsync(() -> {
            try {
                orderFacade.placeOrder(command4);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        CompletableFuture.allOf(thread1, thread2, thread3, thread4).join();

        // Then
        var orders = orderJpaRepository.findAll();
        assertEquals(4, orders.size());

        var updatedProduct = productJpaRepository.findById(product.getId()).orElseThrow();
        assertEquals(60, updatedProduct.getStock());
    }

    @Test
    @DisplayName("여러 스레드에서 상품 주문 요청 시 같은 쿠폰 요청이 동시에 들어오면, 하나의 주문에만 쿠폰이 적용되고 정상 처리 되어야 한다.")
    void testConcurrentOrderWithSameCoupon() {
        // Given
        var user = userJpaRepository.saveAndFlush(Instancio.of(User.class)
                .set(field("id"), null)
                .create());

        var product = productJpaRepository.saveAndFlush(Instancio.of(Product.class)
                .set(field("id"), null)
                .set(field("stock"), 100)
                .set(field("price"), 1000)
                .create());

        var coupon = couponJpaRepository.saveAndFlush(Instancio.of(Coupon.class)
                .set(field("id"), null)
                .set(field("stock"), 100)
                .set(field("discountValue"), BigDecimal.valueOf(1000L))
                .set(field("discountType"), DiscountType.AMOUNT)
                .set(field("startDate"), LocalDateTime.now().minusDays(1))
                .set(field("endDate"), LocalDateTime.now().plusDays(1))
                .create());

        var userCoupon = userCouponJpaRepository.saveAndFlush(Instancio.of(UserCoupon.class)
                .set(field("id"), null)
                .set(field("userId"), user.getId())
                .set(field("coupon"), coupon)
                .set(field("isUsed"), false)
                .create());

        PlaceOrderCommand command1 = new PlaceOrderCommand(user.getId(), userCoupon.getId(),
                List.of(new PlaceOrderCommand.PlaceOrderItem(product.getId(), 10)));
        PlaceOrderCommand command2 = new PlaceOrderCommand(user.getId(), userCoupon.getId(),
                List.of(new PlaceOrderCommand.PlaceOrderItem(product.getId(), 10)));
        PlaceOrderCommand command3 = new PlaceOrderCommand(user.getId(), userCoupon.getId(),
                List.of(new PlaceOrderCommand.PlaceOrderItem(product.getId(), 10)));
        PlaceOrderCommand command4 = new PlaceOrderCommand(user.getId(), userCoupon.getId(),
                List.of(new PlaceOrderCommand.PlaceOrderItem(product.getId(), 10)));

        // When
        CompletableFuture<Void> thread1 = CompletableFuture.runAsync(() -> {
            try {
                orderFacade.placeOrder(command1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        CompletableFuture<Void> thread2 = CompletableFuture.runAsync(() -> {
            try {
                orderFacade.placeOrder(command2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        CompletableFuture<Void> thread3 = CompletableFuture.runAsync(() -> {
            try {
                orderFacade.placeOrder(command3);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        CompletableFuture<Void> thread4 = CompletableFuture.runAsync(() -> {
            try {
                orderFacade.placeOrder(command4);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        CompletableFuture.allOf(thread1, thread2, thread3, thread4).join();

        // Then
        var orders = orderJpaRepository.findAll();
        assertEquals(1, orders.size());
        var updatedCoupon = userCouponJpaRepository.findById(userCoupon.getId()).orElseThrow();
        assertTrue(updatedCoupon.isUsed());
        var updatedProduct = productJpaRepository.findById(product.getId()).orElseThrow();
        assertEquals(90, updatedProduct.getStock());
    }

    @Test
    @DisplayName("여러 스레드에서 상품 주문 요청 시 서로 다른 쿠폰 요청이 동시에 들어오면, 각각의 주문에 쿠폰이 적용되어야 한다.")
    void testConcurrentOrderWithDifferentCoupons() {
        // Given
        var user = userJpaRepository.saveAndFlush(Instancio.of(User.class)
                .set(field("id"), null)
                .create());

        var product = productJpaRepository.saveAndFlush(Instancio.of(Product.class)
                .set(field("id"), null)
                .set(field("stock"), 100)
                .set(field("price"), 1000)
                .create());

        var coupon1 = couponJpaRepository.saveAndFlush(Instancio.of(Coupon.class)
                .set(field("id"), null)
                .set(field("stock"), 100)
                .set(field("discountValue"), BigDecimal.valueOf(1000L))
                .set(field("discountType"), DiscountType.AMOUNT)
                .set(field("startDate"), LocalDateTime.now().minusDays(1))
                .set(field("endDate"), LocalDateTime.now().plusDays(1))
                .create());

        var coupon2 = couponJpaRepository.saveAndFlush(Instancio.of(Coupon.class)
                .set(field("id"), null)
                .set(field("stock"), 100)
                .set(field("discountValue"), BigDecimal.valueOf(500L))
                .set(field("discountType"), DiscountType.AMOUNT)
                .set(field("startDate"), LocalDateTime.now().minusDays(1))
                .set(field("endDate"), LocalDateTime.now().plusDays(1))
                .create());

        var userCoupon1 = userCouponJpaRepository.saveAndFlush(Instancio.of(UserCoupon.class)
                .set(field("id"), null)
                .set(field("userId"), user.getId())
                .set(field("coupon"), coupon1)
                .set(field("isUsed"), false)
                .create());

        var userCoupon2 = userCouponJpaRepository.saveAndFlush(Instancio.of(UserCoupon.class)
                .set(field("id"), null)
                .set(field("userId"), user.getId())
                .set(field("coupon"), coupon2)
                .set(field("isUsed"), false)
                .create());

        PlaceOrderCommand command1 = new PlaceOrderCommand(user.getId(), userCoupon1.getId(),
                List.of(new PlaceOrderCommand.PlaceOrderItem(product.getId(), 10)));
        PlaceOrderCommand command2 = new PlaceOrderCommand(user.getId(), userCoupon2.getId(),
                List.of(new PlaceOrderCommand.PlaceOrderItem(product.getId(), 10)));
        PlaceOrderCommand command3 = new PlaceOrderCommand(user.getId(), userCoupon1.getId(),
                List.of(new PlaceOrderCommand.PlaceOrderItem(product.getId(), 10)));
        PlaceOrderCommand command4 = new PlaceOrderCommand(user.getId(), userCoupon2.getId(),
                List.of(new PlaceOrderCommand.PlaceOrderItem(product.getId(), 10)));

        // When
        CompletableFuture<Void> thread1 = CompletableFuture.runAsync(() -> {
            try {
                orderFacade.placeOrder(command1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        CompletableFuture<Void> thread2 = CompletableFuture.runAsync(() -> {
            try {
                orderFacade.placeOrder(command2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        CompletableFuture<Void> thread3 = CompletableFuture.runAsync(() -> {
            try {
                orderFacade.placeOrder(command3);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        CompletableFuture<Void> thread4 = CompletableFuture.runAsync(() -> {
            try {
                orderFacade.placeOrder(command4);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        CompletableFuture.allOf(thread1, thread2, thread3, thread4).join();

        // Then
        var orders = orderJpaRepository.findAll();
        assertEquals(2, orders.size());
        var updatedCoupon1 = userCouponJpaRepository.findById(userCoupon1.getId()).orElseThrow();
        var updatedCoupon2 = userCouponJpaRepository.findById(userCoupon2.getId()).orElseThrow();
        assertTrue(updatedCoupon1.isUsed());
        assertTrue(updatedCoupon2.isUsed());
        var updatedProduct = productJpaRepository.findById(product.getId()).orElseThrow();
        assertEquals(80, updatedProduct.getStock());
    }

    @Test
    @DisplayName("여러 스레드에서 동시에 주문 취소 요청 시, 하나의 요청만 성공하며 상품 재고 관리가 정확하게 되어야 한다.")
    void testConcurrentCancelOrder() {
        // Given
        var user = userJpaRepository.saveAndFlush(Instancio.of(User.class)
                .set(field("id"), null)
                .create());

        var product = productJpaRepository.saveAndFlush(Instancio.of(Product.class)
                .set(field("id"), null)
                .set(field("stock"), 100)
                .create());

        var order = orderFacade.placeOrder(new PlaceOrderCommand(user.getId(), null,
                List.of(new PlaceOrderCommand.PlaceOrderItem(product.getId(), 10))));

        CancelOrderCommand command1 = new CancelOrderCommand(order.orderId());
        CancelOrderCommand command2 = new CancelOrderCommand(order.orderId());
        CancelOrderCommand command3 = new CancelOrderCommand(order.orderId());
        CancelOrderCommand command4 = new CancelOrderCommand(order.orderId());

        // When
        CompletableFuture<Void> thread1 = CompletableFuture.runAsync(() -> {
            try {
                orderFacade.cancelOrder(command1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        CompletableFuture<Void> thread2 = CompletableFuture.runAsync(() -> {
            try {
                orderFacade.cancelOrder(command2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        CompletableFuture<Void> thread3 = CompletableFuture.runAsync(() -> {
            try {
                orderFacade.cancelOrder(command3);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        CompletableFuture<Void> thread4 = CompletableFuture.runAsync(() -> {
            try {
                orderFacade.cancelOrder(command4);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        CompletableFuture.allOf(thread1, thread2, thread3, thread4).join();

        // Then
        var updatedProduct = productJpaRepository.findById(product.getId()).orElseThrow();
        assertEquals(110, updatedProduct.getStock());
    }
}