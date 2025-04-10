package kr.hhplus.be.server.domain.coupon.command;

import kr.hhplus.be.server.domain.coupon.error.InvalidCouponIdError;
import kr.hhplus.be.server.domain.coupon.error.InvalidUserIdError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IssueCouponCommandTest {

    @Test
    @DisplayName("IssueCouponCommand 생성 시, 사용자 ID가 0 이하인 경우 InvalidUserIdError 예외가 발생한다.")
    void testInvalidCouponIdError() {
        // given
        long couponId = 1;
        long userId = 0;

        // when & then
        assertThrows(InvalidUserIdError.class, () -> new IssueCouponCommand(couponId, userId));
    }

    @Test
    @DisplayName("IssueCouponCommand 생성 시, 쿠폰 ID가 0 이하인 경우 InvalidCouponIdError 예외가 발생한다.")
    void testInvalidUserIdError() {
        // given
        long couponId = 0;
        long userId = 1;

        // when & then
        assertThrows(InvalidCouponIdError.class, () -> new IssueCouponCommand(couponId, userId));
    }

    @Test
    @DisplayName("IssueCouponCommand 생성 시, 쿠폰 ID와 사용자 ID가 0보다 큰 경우 정상적으로 생성된다.")
    void testValidCouponId() {
        // given
        long couponId = 1;
        long userId = 1;

        // when
        IssueCouponCommand command = new IssueCouponCommand(couponId, userId);

        // then
        assertEquals(couponId, command.couponId());
        assertEquals(userId, command.userId());
    }


}