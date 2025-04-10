package kr.hhplus.be.server.domain.coupon.command;

import kr.hhplus.be.server.domain.coupon.error.InvalidAmountError;
import kr.hhplus.be.server.domain.coupon.error.InvalidCouponIdError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UseCouponCommandTest {

    @Test
    @DisplayName("UseCouponCommand 생성 시, 쿠폰 ID가 0 이하인 경우 InvalidCouponIdError 예외가 발생한다.")
    void testInvalidCouponIdError() {
        // given
        long invalidCouponId = 0;
        int amount = 100;

        // when & then
        assertThrows(InvalidCouponIdError.class, () -> new UseCouponCommand(invalidCouponId, amount));
    }

    @Test
    @DisplayName("UseCouponCommand 생성 시, 금액이 0 이하인 경우 InvalidAmountError 예외가 발생한다.")
    void testInvalidAmountError() {
        // given
        long couponId = 1;
        int invalidAmount = 0;

        // when & then
        assertThrows(InvalidAmountError.class, () -> new UseCouponCommand(couponId, invalidAmount));
    }

    @Test
    @DisplayName("UseCouponCommand 생성 시, 쿠폰 ID와 금액이 0보다 큰 경우 정상적으로 생성된다.")
    void testValidCouponIdAndAmount() {
        // given
        long couponId = 1;
        int amount = 100;

        // when
        UseCouponCommand command = new UseCouponCommand(couponId, amount);

        // then
        assertEquals(couponId, command.userCouponId());
        assertEquals(amount, command.amount());
    }

}