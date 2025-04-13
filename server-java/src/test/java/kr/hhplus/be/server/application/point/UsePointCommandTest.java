package kr.hhplus.be.server.application.point;

import kr.hhplus.be.server.ApiError;
import kr.hhplus.be.server.ApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsePointCommandTest {

    @Test
    @DisplayName("유효한 userId와 amount로 UsePointCommand를 생성하면 성공한다.")
    void validUsePointCommand() {
        long userId = 1L;
        int amount = 1000;

        UsePointCommand command = new UsePointCommand(userId, amount);

        assertEquals(userId, command.userId());
        assertEquals(amount, command.amount());
    }

    @Test
    @DisplayName("userId가 0 이하인 경우 UsePointCommand를 생성하면 ApiException(INVALID_USER_ID)이 발생한다.")
    void invalidUserId() {
        long userId = 0L;
        int amount = 1000;

        ApiException exception = assertThrows(ApiException.class, () -> new UsePointCommand(userId, amount));

        assertEquals(ApiError.INVALID_USER_ID, exception.getApiError());
    }

    @Test
    @DisplayName("amount가 0 이하인 경우 UsePointCommand를 생성하면 ApiException(INVALID_USE_AMOUNT)이 발생한다.")
    void invalidChargeAmount() {
        long userId = 1L;
        int amount = 0;

        ApiException exception = assertThrows(ApiException.class, () -> new UsePointCommand(userId, amount));

        assertEquals(ApiError.INVALID_USE_AMOUNT, exception.getApiError());
    }

}