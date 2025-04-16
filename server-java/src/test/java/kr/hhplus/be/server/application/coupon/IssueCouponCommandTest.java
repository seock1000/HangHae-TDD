package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.ApiError;
import kr.hhplus.be.server.ApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IssueCouponCommandTest {

    @Test
    @DisplayName("유효한 userId와 couponId로 IssueCouponCommand를 생성하면 성공한다.")
    void validIssueCouponCommand() {
        long userId = 1L;
        long couponId = 1L;

        IssueCouponCommand command = new IssueCouponCommand(userId, couponId);

        assertEquals(userId, command.userId());
        assertEquals(couponId, command.couponId());
    }

    @Test
    @DisplayName("userId가 0 이하인 경우 IssueCouponCommand를 생성하면 ApiException(INVALID_USER_ID)이 발생한다.")
    void invalidUserId() {
        long userId = 0L;
        long couponId = 1L;

        ApiException exception = assertThrows(ApiException.class, () -> new IssueCouponCommand(userId, couponId));

        assertEquals(ApiError.INVALID_USER_ID, exception.getApiError());
    }

    @Test
    @DisplayName("couponId가 0 이하인 경우 IssueCouponCommand를 생성하면 ApiException(INVALID_COUPON_ID)이 발생한다.")
    void invalidCouponId() {
        long userId = 1L;
        long couponId = 0L;

        ApiException exception = assertThrows(ApiException.class, () -> new IssueCouponCommand(userId, couponId));

        assertEquals(ApiError.INVALID_COUPON_ID, exception.getApiError());
    }

}