package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.ApiError;
import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.domain.coupon.IssuedCoupon;
import kr.hhplus.be.server.domain.product.OrderedProduct;
import kr.hhplus.be.server.domain.user.User;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Optional;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class OrdersTest {

    @Test
    @DisplayName("주문 생성 시, 주문번호와 주문자를 받아 대기 상태로 주문을 생성한다.")
    void createOrder() {
        // given
        String orderId = "order-12345";
        User user = Instancio.create(User.class);

        // when
        Orders orders = Orders.createWithIdAndUser(orderId, user);

        // then
        assertEquals(orderId, orders.getId());
        assertEquals(user.getId(), orders.getUser());
        assertEquals(OrderStatus.PENDING, orders.getStatus());
        assertNull(orders.getCouponId());
        assertEquals(0, orders.getTotalAmount());
        assertEquals(0, orders.getDiscountAmount());
        assertTrue(orders.getOrderItems().isEmpty());
    }

    @Test
    @DisplayName("주문에 상품을 추가하면, 주문의 총 금액과 주문 항목이 업데이트된다.")
    void addProduct() {
        // given
        Orders orders = Instancio.of(Orders.class)
                .set(field("totalAmount"), 0)
                .set(field("orderItems"), new ArrayList<>())
                .create();
        OrderedProduct product = Mockito.mock(OrderedProduct.class);
        when(product.getPrice()).thenReturn(10000);
        int quantity = 2;

        // when
        orders.addProduct(product, quantity);

        // then
        Mockito.verify(product).deductStock(quantity);
        assertEquals(orders.getTotalAmount(), product.getPrice() * quantity);
        assertEquals(1, orders.getOrderItems().size());
        assertEquals(orders.getOrderItems().get(0).getProductId(), product.getId());
        assertEquals(orders.getOrderItems().get(0).getQuantity(), quantity);
    }

    @Test
    @DisplayName("이미 적용된 쿠폰이 존재할 경우 ApiException(ORDER_ALREADY_COUPON_APPLIED) 예외가 발생한다.")
    void applyCouponFail() {
        // given
        Orders orders = Instancio.of(Orders.class)
                .set(field("couponId"), 1L)
                .create();
        IssuedCoupon coupon = Mockito.mock(IssuedCoupon.class);

        // when & then
        ApiException exception = assertThrows(ApiException.class, () -> orders.applyCoupon(coupon));
        assertEquals(ApiError.ORDER_ALREADY_COUPON_APPLIED, exception.getApiError());
    }

    @Test
    @DisplayName("쿠폰 할인 금액이 총 주문 금액보다 많을 시, ApiException(ORDER_COUPON_DISCOUNT_AMOUNT_EXCEEDS_TOTAL_AMOUNT) 예외가 발생한다.")
    void applyCouponFail2() {
        // given
        Orders orders = Instancio.of(Orders.class)
                .set(field("couponId"), null)
                .set(field("totalAmount"), 1000)
                .set(field("discountAmount"), 0)
                .create();
        IssuedCoupon coupon = Mockito.mock(IssuedCoupon.class);
        when(coupon.discount(anyInt())).thenReturn(2000);

        // when & then
        ApiException exception = assertThrows(ApiException.class, () -> orders.applyCoupon(coupon));
        assertEquals(ApiError.ORDER_COUPON_DISCOUNT_AMOUNT_EXCEEDS_TOTAL_AMOUNT, exception.getApiError());
    }

    @Test
    @DisplayName("주문을 확정하면 주문 상태가 PENDING에서 CONFIRMED로 변경된다.")
    void confirmOrder() {
        // given
        Orders orders = Instancio.of(Orders.class)
                .set(field("status"), OrderStatus.PENDING)
                .create();

        // when
        orders.confirm();

        // then
        assertEquals(OrderStatus.CONFIRMED, orders.getStatus());
    }

    @Test
    @DisplayName("주문이 PENDING이 아닌 상태에서 주문을 확정하면 ORDER_CANNOT_BE_CONFIRMED 예외가 발생한다.")
    void confirmOrderFail() {
        // given
        Orders orders = Instancio.of(Orders.class)
                .set(field("status"), OrderStatus.CANCELLED)
                .create();

        // when & then
        ApiException exception = assertThrows(ApiException.class, orders::confirm);
        assertEquals(ApiError.ORDER_CANNOT_BE_CONFIRMED, exception.getApiError());
    }

    @Test
    @DisplayName("주문을 취소하면 주문 상태가 PENDING에서 CANCELLED로 변경된다.")
    void cancelOrder() {
        // given
        Orders orders = Instancio.of(Orders.class)
                .set(field("status"), OrderStatus.PENDING)
                .create();

        // when
        orders.cancel();

        // then
        assertEquals(OrderStatus.CANCELLED, orders.getStatus());
    }

    @Test
    @DisplayName("주문이 PENDING이 아닌 상태에서 주문을 취소하면 ORDER_CANNOT_BE_CANCELED 예외가 발생한다.")
    void cancelOrderFail() {
        // given
        Orders orders = Instancio.of(Orders.class)
                .set(field("status"), OrderStatus.CONFIRMED)
                .create();

        // when & then
        ApiException exception = assertThrows(ApiException.class, orders::cancel);
        assertEquals(ApiError.ORDER_CANNOT_BE_CANCELED, exception.getApiError());
    }


}