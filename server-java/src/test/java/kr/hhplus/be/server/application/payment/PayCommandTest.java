package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.ApiError;
import kr.hhplus.be.server.ApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PayCommandTest {

    @Test
    @DisplayName("유효한 orderId로 PayCommand를 생성하면 성공한다.")
    void validPayCommand() {
        String orderId = "12345";

        PayCommand command = new PayCommand(orderId);

        assertEquals(orderId, command.orderId());
    }

    @Test
    @DisplayName("orderId가 null인 경우 PayCommand를 생성하면 ApiException(INVALID_ORDER_ID)이 발생한다.")
    void nullOrderId() {
        String orderId = null;

        ApiException exception = assertThrows(ApiException.class, () -> new PayCommand(orderId));

        assertEquals(ApiError.INVALID_ORDER_ID, exception.getApiError());
    }

    @Test
    @DisplayName("orderId가 빈 문자열인 경우 PayCommand를 생성하면 ApiException(INVALID_ORDER_ID)이 발생한다.")
    void emptyOrderId() {
        String orderId = "";

        ApiException exception = assertThrows(ApiException.class, () -> new PayCommand(orderId));

        assertEquals(ApiError.INVALID_ORDER_ID, exception.getApiError());
    }
}