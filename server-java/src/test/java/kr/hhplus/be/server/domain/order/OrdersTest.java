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
    @DisplayName("주문 객체에 plusTotalAmount 메서드를 호출하면 amount 만큼 총 주문금액이 증가한다.")
    void plusTotalAmountTest() {
        // given
        String orderId = "orderId";
        Long userId = 1L;
        Orders order = Orders.createWithIdAndUser(orderId, userId);

        // when
        order.plusTotalAmount(1000);

        // then
        assertEquals(1000, order.getTotalAmount());
    }

    @Test
    @DisplayName("주문 객체에 minusTotalAmount 메서드를 호출하면 amount 만큼 총 주문금액이 감소한다.")
    void minusTotalAmountTest() {
        // given
        String orderId = "orderId";
        Long userId = 1L;
        Orders order = Orders.createWithIdAndUser(orderId, userId);
        order.plusTotalAmount(1000);

        // when
        order.minusTotalAmount(1000);

        // then
        assertEquals(0, order.getTotalAmount());
    }

    @Test
    @DisplayName("주문 객체에 minusTotalAmount 메서드를 호출할 때, 총 주문금액이 부족하면 InsufficientTotalAmountError 예외가 발생한다.")
    void minusTotalAmountInsufficientTest() {
        // given
        String orderId = "orderId";
        Long userId = 1L;
        Orders order = Orders.createWithIdAndUser(orderId, userId);
        order.plusTotalAmount(1000);

        // when & then
        InsufficientTotalAmountError exception = assertThrows(InsufficientTotalAmountError.class, () -> {
            order.minusTotalAmount(1001);
        });

        assertEquals("총 주문금액은 0원 이상이어야 합니다.", exception.getMessage());
    }

}