package kr.hhplus.be.server.domain.order.command;

import kr.hhplus.be.server.domain.order.error.InvalidOrderIdError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CancelOrderHandlerCommandTest {

    @Test
    @DisplayName("CancelOrderHandlerCommand 생성시 주문번호가 null 또는 빈 문자열이 아니면 정상적으로 생성된다.")
    void testValidOrderId() {
        // given
        String orderId = "12345";

        // when
        CancelOrderHandlerCommand command = new CancelOrderHandlerCommand(orderId);

        // then
        assertEquals(orderId, command.orderId());
    }

    @Test
    @DisplayName("CancelOrderHandlerCommand 생성시 주문번호가 null이면 InvalidOrderIdError가 발생한다.")
    void testNullOrderId() {
        // given
        String orderId = null;

        // when & then
        Exception exception = assertThrows(InvalidOrderIdError.class, () -> {
            new CancelOrderHandlerCommand(orderId);
        });

        assertEquals("주문번호는 필수입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("CancelOrderHandlerCommand 생성시 주문번호가 빈 문자열이면 InvalidOrderIdError가 발생한다.")
    void testEmptyOrderId() {
        // given
        String orderId = "";

        // when & then
        Exception exception = assertThrows(InvalidOrderIdError.class, () -> {
            new CancelOrderHandlerCommand(orderId);
        });

        assertEquals("주문번호는 필수입니다.", exception.getMessage());
    }

}