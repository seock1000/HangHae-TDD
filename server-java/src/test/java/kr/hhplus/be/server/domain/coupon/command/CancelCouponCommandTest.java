package kr.hhplus.be.server.domain.coupon.command;

import kr.hhplus.be.server.domain.coupon.error.InvalidCouponIdError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CancelCouponCommandTest {

    @Test
    @DisplayName("CancelCouponCommand 생성 시, 쿠폰 ID가 0 이하인 경우 InvalidCouponIdError 예외가 발생한다.")
    void testInvalidCouponIdError() {
        // given
        long invalidCouponId = 0;

        // when & then
        assertThrows(InvalidCouponIdError.class, () -> new CancelCouponCommand(invalidCouponId));
    }

    @Test
    @DisplayName("CancelCouponCommand 생성 시, 쿠폰 ID가 0보다 큰 경우 정상적으로 생성된다.")
    void testValidCouponId() {
        // given
        long validCouponId = 1;

        // when
        CancelCouponCommand command = new CancelCouponCommand(validCouponId);

        // then
        assertEquals(validCouponId, command.userCouponId());
    }

}