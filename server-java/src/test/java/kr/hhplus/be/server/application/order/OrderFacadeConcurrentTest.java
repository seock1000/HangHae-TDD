package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.IntegrationTestSupport;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.DiscountType;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.order.Orders;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.infrastructure.coupon.persistence.CouponJpaRepository;
import kr.hhplus.be.server.infrastructure.coupon.persistence.UserCouponJpaRepository;
import kr.hhplus.be.server.infrastructure.order.OrderJpaRepository;
import kr.hhplus.be.server.infrastructure.product.ProductJpaRepository;
import kr.hhplus.be.server.infrastructure.user.UserJpaRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

class OrderFacadeConcurrentTest extends IntegrationTestSupport {

    @Autowired
    private OrderFacade orderFacade;
    @Autowired
    private OrderJpaRepository orderJpaRepository;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private UserCouponJpaRepository userCouponJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private ProductJpaRepository productJpaRepository;


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
    @DisplayName("여러 스레드에서 동시에 상품 주문 요청 시, 상품 재고를 초과한 주문이 발생하지 않아야 한다.")
    void 여러_주문이_동시에_발생할때_주문이_상품재고보다_초과발생하지_않는다() {
        // Given
        int remainStock = 3;
        var user = userJpaRepository.saveAndFlush(Instancio.of(User.class)
                .set(field("id"), null)
                .create());

        var product = productJpaRepository.saveAndFlush(Instancio.of(Product.class)
                .set(field("id"), null)
                .set(field("stock"), remainStock)
                .create());

        List<PlaceOrderCommand> commands = List.of(
                new PlaceOrderCommand(user.getId(), null,
                        List.of(new PlaceOrderCommand.PlaceOrderItem(product.getId(), 1))),
                new PlaceOrderCommand(user.getId(), null,
                        List.of(new PlaceOrderCommand.PlaceOrderItem(product.getId(), 1))),
                new PlaceOrderCommand(user.getId(), null,
                        List.of(new PlaceOrderCommand.PlaceOrderItem(product.getId(), 1))),
                new PlaceOrderCommand(user.getId(), null,
                        List.of(new PlaceOrderCommand.PlaceOrderItem(product.getId(), 1))),
                new PlaceOrderCommand(user.getId(), null,
                        List.of(new PlaceOrderCommand.PlaceOrderItem(product.getId(), 1)))
        );
        AtomicInteger successCount = new AtomicInteger(0);

        // When
        List<CompletableFuture<Void>> threads = commands.stream()
                .map(command -> CompletableFuture.runAsync(() -> {
                    try {
                        orderFacade.placeOrder(command);
                        successCount.incrementAndGet();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }))
                .toList();

        CompletableFuture.allOf(threads.toArray(new CompletableFuture[0])).join();

        // Then
        var orders = orderJpaRepository.findAll();
        assertEquals(3, orders.size());
        assertEquals(remainStock, successCount.get());
        var updatedProduct = productJpaRepository.findById(product.getId()).orElseThrow();
        assertEquals(0, updatedProduct.getStock());
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

        var coupon = couponRepository.saveCoupon(Instancio.of(Coupon.class)
                .set(field("id"), null)
                .set(field("stock"), 100)
                .set(field("discountValue"), BigDecimal.valueOf(1000L))
                .set(field("discountType"), DiscountType.AMOUNT)
                .set(field("startDate"), LocalDateTime.now().minusDays(1))
                .set(field("endDate"), LocalDateTime.now().plusDays(1))
                .create());

        var userCoupon = couponRepository.saveUserCoupon(Instancio.of(UserCoupon.class)
                .set(field("id"), null)
                .set(field("userId"), user.getId())
                .set(field("coupon"), coupon)
                .set(field("isUsed"), false)
                .create());

        var commands = List.of(
                new PlaceOrderCommand(user.getId(), userCoupon.getId(),
                        List.of(new PlaceOrderCommand.PlaceOrderItem(product.getId(), 10))),
                new PlaceOrderCommand(user.getId(), userCoupon.getId(),
                        List.of(new PlaceOrderCommand.PlaceOrderItem(product.getId(), 10))),
                new PlaceOrderCommand(user.getId(), userCoupon.getId(),
                        List.of(new PlaceOrderCommand.PlaceOrderItem(product.getId(), 10))),
                new PlaceOrderCommand(user.getId(), userCoupon.getId(),
                        List.of(new PlaceOrderCommand.PlaceOrderItem(product.getId(), 10)))
        );
        AtomicInteger counter = new AtomicInteger(0);

        // When
        List<CompletableFuture<Void>> threads = commands.stream()
                .map(command -> CompletableFuture.runAsync(() -> {
                    try {
                        orderFacade.placeOrder(command);
                        counter.incrementAndGet();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }))
                .toList();

        CompletableFuture.allOf(threads.toArray(new CompletableFuture[0])).join();

        // Then
        var orders = orderJpaRepository.findAll();
        assertEquals(1, orders.size());
        var updatedCoupon = userCouponJpaRepository.findById(userCoupon.getId()).orElseThrow();
        assertTrue(updatedCoupon.isUsed());
        assertEquals(1, counter.get());
        var updatedProduct = productJpaRepository.findById(product.getId()).orElseThrow();
        assertEquals(90, updatedProduct.getStock());
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

        var order = Orders.createWithIdAndUser("orderId", user);
        order.addProduct(product.toOrderedProduct(), 10);
        var savedOrder = orderJpaRepository.save(order);

        var commands = List.of(
                new CancelOrderCommand(savedOrder.getId()),
                new CancelOrderCommand(savedOrder.getId()),
                new CancelOrderCommand(savedOrder.getId()),
                new CancelOrderCommand(savedOrder.getId())
        );
        AtomicInteger counter = new AtomicInteger(0);

        // When
        List<CompletableFuture<Void>> threads = commands.stream()
                .map(command -> CompletableFuture.runAsync(() -> {
                    try {
                        orderFacade.cancelOrder(command);
                        counter.incrementAndGet();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }))
                .toList();

        CompletableFuture.allOf(threads.toArray(new CompletableFuture[0])).join();

        // Then
        var updatedProduct = productJpaRepository.findById(product.getId()).orElseThrow();
        assertEquals(110, updatedProduct.getStock());
        assertEquals(1, counter.get()); // 주문 취소는 한 번만 가능하므로, 성공한 요청의 수는 1이어야 한다.
    }

    @Test
    @DisplayName("여러 스레드에서 동시에 같은 상품에 대해 주문과 주문 취소 요청이 이루어질 때, 재고 관리가 정확하게 되어야 한다.")
    void testConcurrentOrderAndCancelOrder() {
        var user = userJpaRepository.saveAndFlush(Instancio.of(User.class)
                .set(field("id"), null)
                .create());

        var product = productJpaRepository.saveAndFlush(Instancio.of(Product.class)
                .set(field("id"), null)
                .set(field("stock"), 100)
                .create());

        var order = Orders.createWithIdAndUser("orderId", user);
        order.addProduct(product.toOrderedProduct(), 10);
        var order2 = Orders.createWithIdAndUser("orderId2", user);
        order.addProduct(product.toOrderedProduct(), 10);
        var savedOrder = orderJpaRepository.save(order);
        var savedOrder2 = orderJpaRepository.save(order2);

        var cancelCommands = List.of(
                new CancelOrderCommand(savedOrder.getId()),
                new CancelOrderCommand(savedOrder2.getId())
        );
        var orderCommands = List.of(
                new PlaceOrderCommand(user.getId(), null,
                        List.of(new PlaceOrderCommand.PlaceOrderItem(product.getId(), 10))),
                new PlaceOrderCommand(user.getId(), null,
                        List.of(new PlaceOrderCommand.PlaceOrderItem(product.getId(), 10)))
        );
        AtomicInteger counter = new AtomicInteger(0);

        // When
        List<CompletableFuture<Void>> threads = new ArrayList<>();
        for (var command : cancelCommands) {
            threads.add(CompletableFuture.runAsync(() -> {
                try {
                    orderFacade.cancelOrder(command);
                    counter.incrementAndGet();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));
        }
        for (var command : orderCommands) {
            threads.add(CompletableFuture.runAsync(() -> {
                try {
                    orderFacade.placeOrder(command);
                    counter.incrementAndGet();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));
        }

        CompletableFuture.allOf(threads.toArray(new CompletableFuture[0])).join();

        var resultProduct = productJpaRepository.findById(product.getId()).orElseThrow();
        assertEquals(100, resultProduct.getStock());
    }
}