package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.ApiError;
import kr.hhplus.be.server.ApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GetUserCouponCommandTest {

    @Test
    @DisplayName("유효한 userId로 GetUserCouponCommand를 생성하면 성공한다.")
    void validGetUserCouponCommand() {
        long userId = 1L;

        GetUserCouponCommand command = new GetUserCouponCommand(userId);

        assertEquals(userId, command.userId());
    }

    @Test
    @DisplayName("userId가 0 이하인 경우 GetUserCouponCommand를 생성하면 ApiException(INVALID_USER_ID)이 발생한다.")
    void invalidUserId() {
        long userId = 0L;

        ApiException exception = assertThrows(ApiException.class, () -> new GetUserCouponCommand(userId));

        assertEquals(ApiError.INVALID_USER_ID, exception.getApiError());
    }

}