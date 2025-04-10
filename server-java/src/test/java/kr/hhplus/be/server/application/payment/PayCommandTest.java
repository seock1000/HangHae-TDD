package kr.hhplus.be.server.application.payment;

import net.bytebuddy.utility.dispatcher.JavaDispatcher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PayCommandTest {

    @Test
    @DisplayName("PayCommand 객체 생성 시, 주문 ID가 null이거나 빈 문자열이 아니면 PayCommand 객체를 생성한다.")
    void testValidPayCommand() {
        // given
        String orderId = "12345";

        // when
        PayCommand payCommand = new PayCommand(orderId);

        // then
        assertEquals(orderId, payCommand.orderId());
    }

    @Test
    @DisplayName("PayCommand 객체 생성 시, 주문 ID가 null이면 InvalidOrderIdError 예외를 던진다.")
    void testNullOrderId() {
        // given
        String orderId = null;

        // when & then
        Exception exception = assertThrows(InvalidOrderIdError.class, () -> new PayCommand(orderId));
        assertEquals("주문 ID는 필수입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("PayCommand 객체 생성 시, 주문 ID가 빈 문자열이면 InvalidOrderIdError 예외를 던진다.")
    void testEmptyOrderId() {
        // given
        String orderId = "";

        // when & then
        Exception exception = assertThrows(InvalidOrderIdError.class, () -> new PayCommand(orderId));
        assertEquals("주문 ID는 필수입니다.", exception.getMessage());
    }

}