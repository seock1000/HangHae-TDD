package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.order.error.InsufficientTotalAmountError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrdersTest {

    @Test
    @DisplayName("주문 생성 시, 주문번호와 사용자 ID를 전달하면 총 주문금액은 0원, 주문상태는 PENDING으로 생성된다.")
    void createOrderTest() {
        // given
        String orderId = "orderId";
        Long userId = 1L;

        // when
        Orders order = Orders.createWithIdAndUser(orderId, userId);

        assertNotNull(order);
        assertEquals(orderId, order.getId());
        assertEquals(userId, order.getUser());
        assertEquals(0, order.getTotalAmount());
        assertEquals(OrderStatus.PENDING, order.getStatus());
    }

    @Test
    @DisplayName("주문 객체에 OrderItem을 추가하면 총 OrderItem과 주문금액이 증가한다.")
    void plusTotalAmountTest() {
        // given
        String orderId = "orderId";
        Long userId = 1L;
        Orders order = Orders.createWithIdAndUser(orderId, userId);
        OrderItem orderItem = OrderItem.create(orderId, 1L, 1000, 2);
        int expectedTotalAmount = 2000;

        // when
        order.addOrderItem(orderItem);

        // then
        assertEquals(1, order.getOrderItems().size());
        assertEquals(expectedTotalAmount, order.getTotalAmount());
    }

    @Test
    @DisplayName("주문 객체에 minusTotalAmount 메서드를 호출하면 amount 만큼 총 주문금액이 감소한다.")
    void minusTotalAmountTest() {
        // given
        String orderId = "orderId";
        Long userId = 1L;
        Orders order = Orders.createWithIdAndUser(orderId, userId);
        OrderItem orderItem = OrderItem.create(orderId, 1L, 1000, 1);
        order.addOrderItem(orderItem);
        int expectedTotalAmount = 0;

        // when
        order.minusTotalAmount(1000);

        // then
        assertEquals(expectedTotalAmount, order.getTotalAmount());
    }

    @Test
    @DisplayName("주문 객체에 minusTotalAmount 메서드를 호출할 때, 총 주문금액이 부족하면 InsufficientTotalAmountError 예외가 발생한다.")
    void minusTotalAmountInsufficientTest() {
        // given
        String orderId = "orderId";
        Long userId = 1L;
        Orders order = Orders.createWithIdAndUser(orderId, userId);
        OrderItem orderItem = OrderItem.create(orderId, 1L, 1000, 1);
        order.addOrderItem(orderItem);

        // when & then
        InsufficientTotalAmountError exception = assertThrows(InsufficientTotalAmountError.class, () -> {
            order.minusTotalAmount(1001);
        });

        assertEquals("총 주문금액은 0원 이상이어야 합니다.", exception.getMessage());
    }

    @Test
    @DisplayName("주문 객체에 cancel 메서드를 호출하면 주문상태가 CANCELLED로 변경된다.")
    void cancelOrderTest() {
        // given
        String orderId = "orderId";
        Long userId = 1L;
        Orders order = Orders.createWithIdAndUser(orderId, userId);

        // when
        order.cancel();

        // then
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    @DisplayName("주문 객체에 confirm 메서드를 호출하면 주문상태가 CONFIRMED로 변경된다.")
    void confirmOrderTest() {
        // given
        String orderId = "orderId";
        Long userId = 1L;
        Orders order = Orders.createWithIdAndUser(orderId, userId);

        // when
        order.confirm();

        // then
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());
    }

}