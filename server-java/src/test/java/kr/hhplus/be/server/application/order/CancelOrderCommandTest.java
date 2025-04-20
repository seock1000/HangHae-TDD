package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.ApiError;
import kr.hhplus.be.server.ApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CancelOrderCommandTest {

    @Test
    @DisplayName("유효한 orderId로 CancelOrderCommand를 생성하면 성공한다.")
    void validCancelOrderCommand() {
        String orderId = "12345";

        CancelOrderCommand command = new CancelOrderCommand(orderId);

        assertEquals(orderId, command.orderId());
    }

    @Test
    @DisplayName("orderId가 null인 경우 CancelOrderCommand를 생성하면 ApiException(INVALID_ORDER_ID)이 발생한다.")
    void nullOrderId() {
        String orderId = null;

        ApiException exception = assertThrows(ApiException.class, () -> new CancelOrderCommand(orderId));

        assertEquals(ApiError.INVALID_ORDER_ID, exception.getApiError());
    }

    @Test
    @DisplayName("orderId가 빈 문자열인 경우 CancelOrderCommand를 생성하면 ApiException(INVALID_ORDER_ID)이 발생한다.")
    void emptyOrderId() {
        String orderId = "";

        ApiException exception = assertThrows(ApiException.class, () -> new CancelOrderCommand(orderId));

        assertEquals(ApiError.INVALID_ORDER_ID, exception.getApiError());
    }

}