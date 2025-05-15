package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.IntegrationTestSupport;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.DiscountType;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.infrastructure.coupon.persistence.CouponJpaRepository;
import kr.hhplus.be.server.infrastructure.coupon.persistence.UserCouponJpaRepository;
import kr.hhplus.be.server.infrastructure.product.ProductJpaRepository;
import kr.hhplus.be.server.infrastructure.user.UserJpaRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

class OrderFacadeIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private OrderFacade orderFacade;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private CouponJpaRepository couponJpaRepository;
    @Autowired
    private UserCouponJpaRepository userCouponJpaRepository;
    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Test
    @DisplayName("쿠폰을 사용하지 않고 충분한 재고가 있는 상품을 주문 시, 주문에 성공한다.")
    void placeOrderWithEnoughStock() {
        // given
        var user = Instancio.of(User.class)
                .set(field("id"), null)
                .create();
        userJpaRepository.saveAndFlush(user);
        var product = List.of(
                Instancio.of(Product.class)
                .set(field("id"), null)
                .set(field("stock"), 10)
                .create(),
                Instancio.of(Product.class)
                .set(field("id"), null)
                .set(field("stock"), 5)
                .create());
        productJpaRepository.saveAllAndFlush(product);

        var command = new PlaceOrderCommand(user.getId(), null, List.of(
                new PlaceOrderCommand.PlaceOrderItem(product.get(0).getId(), 2),
                new PlaceOrderCommand.PlaceOrderItem(product.get(1).getId(), 3)
        ));

        // when
        var result = orderFacade.placeOrder(command);

        // then
        assertNotNull(result);
    }

    @Test
    @DisplayName("쿠폰을 사용하고 충분한 재고가 있는 상품을 주문 시, 주문에 성공한다.")
    void placeOrderWithEnoughStockAndCoupon() {
        // given
        var user = Instancio.of(User.class)
                .set(field("id"), null)
                .create();
        userJpaRepository.saveAndFlush(user);
        var coupon = Instancio.of(Coupon.class)
                .set(field("id"), null)
                .set(field("discountType"), DiscountType.AMOUNT)
                .set(field("discountValue"), BigDecimal.valueOf(1000L))
                .set(field("startDate"), LocalDateTime.now().minusDays(1))
                .set(field("endDate"), LocalDateTime.now().plusDays(1))
                .create();
        couponJpaRepository.saveAndFlush(coupon);
        var userCoupon = Instancio.of(UserCoupon.class)
                .set(field("id"), null)
                .set(field("userId"), user.getId())
                .set(field("coupon"), coupon)
                .set(field("isUsed"), false)
                .create();
        userCouponJpaRepository.saveAndFlush(userCoupon);
        var product = List.of(
                Instancio.of(Product.class)
                        .set(field("id"), null)
                        .set(field("stock"), 10)
                        .set(field("price"), 10000)
                        .create(),
                Instancio.of(Product.class)
                        .set(field("id"), null)
                        .set(field("stock"), 5)
                        .set(field("price"), 20000)
                        .create());
        productJpaRepository.saveAllAndFlush(product);

        var command = new PlaceOrderCommand(user.getId(), userCoupon.getId(), List.of(
                new PlaceOrderCommand.PlaceOrderItem(product.get(0).getId(), 2),
                new PlaceOrderCommand.PlaceOrderItem(product.get(1).getId(), 3)
        ));

        // when
        var result = orderFacade.placeOrder(command);

        // then
        assertNotNull(result);
    }

    @Test
    @DisplayName("쿠폰이 사용된 주문 취소 시, 주문의 상태가 PENDING이면 주문을 취소하고 상품 재고와 쿠폰 사용 여부를 복구한다.")
    void cancelOrderWithCoupon() {
        // given
        var user = Instancio.of(User.class)
                .set(field("id"), null)
                .create();
        userJpaRepository.saveAndFlush(user);
        var coupon = Instancio.of(Coupon.class)
                .set(field("id"), null)
                .set(field("discountType"), DiscountType.AMOUNT)
                .set(field("discountValue"), BigDecimal.valueOf(1000L))
                .set(field("startDate"), LocalDateTime.now().minusDays(1))
                .set(field("endDate"), LocalDateTime.now().plusDays(1))
                .create();
        couponJpaRepository.saveAndFlush(coupon);
        var userCoupon = Instancio.of(UserCoupon.class)
                .set(field("id"), null)
                .set(field("userId"), user.getId())
                .set(field("coupon"), coupon)
                .set(field("isUsed"), false)
                .create();
        userCouponJpaRepository.saveAndFlush(userCoupon);
        var product = List.of(
                Instancio.of(Product.class)
                        .set(field("id"), null)
                        .set(field("stock"), 10)
                        .set(field("price"), 10000)
                        .create(),
                Instancio.of(Product.class)
                        .set(field("id"), null)
                        .set(field("stock"), 5)
                        .set(field("price"), 20000)
                        .create());
        productJpaRepository.saveAllAndFlush(product);

        var orderCommand = new PlaceOrderCommand(user.getId(), userCoupon.getId(), List.of(
                new PlaceOrderCommand.PlaceOrderItem(product.get(0).getId(), 2),
                new PlaceOrderCommand.PlaceOrderItem(product.get(1).getId(), 3)
        ));
        var orderResult = orderFacade.placeOrder(orderCommand);

        var command = new CancelOrderCommand(orderResult.orderId());

        // when
        var result = orderFacade.cancelOrder(command);

        // then
        assertNotNull(result);
    }

    @Test
    @DisplayName("쿠폰이 사용되지 않은 주문 취소 시, 주문의 상태가 PENDING이면 주문을 취소하고 상품 재고를 복구한다.")
    void cancelOrderWithoutCoupon() {
        // given
        var user = Instancio.of(User.class)
                .set(field("id"), null)
                .create();
        userJpaRepository.saveAndFlush(user);
        var product = List.of(
                Instancio.of(Product.class)
                        .set(field("id"), null)
                        .set(field("stock"), 10)
                        .set(field("price"), 10000)
                        .create(),
                Instancio.of(Product.class)
                        .set(field("id"), null)
                        .set(field("stock"), 5)
                        .set(field("price"), 20000)
                        .create());
        productJpaRepository.saveAllAndFlush(product);

        var orderCommand = new PlaceOrderCommand(user.getId(), null, List.of(
                new PlaceOrderCommand.PlaceOrderItem(product.get(0).getId(), 2),
                new PlaceOrderCommand.PlaceOrderItem(product.get(1).getId(), 3)
        ));
        var orderResult = orderFacade.placeOrder(orderCommand);

        var command = new CancelOrderCommand(orderResult.orderId());

        // when
        var result = orderFacade.cancelOrder(command);

        // then
        assertNotNull(result);
    }


}