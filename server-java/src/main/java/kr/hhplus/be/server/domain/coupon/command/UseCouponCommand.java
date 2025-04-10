package kr.hhplus.be.server.domain.coupon.command;

import kr.hhplus.be.server.domain.coupon.error.InvalidAmountError;
import kr.hhplus.be.server.domain.coupon.error.InvalidCouponIdError;
import kr.hhplus.be.server.domain.coupon.error.InvalidUserIdError;

public record UseCouponCommand(
        long userCouponId,
        int amount
) {
    public UseCouponCommand {
        if(userCouponId <= 0) {
            throw InvalidCouponIdError.of("잘못된 쿠폰 ID 형식입니다.");
        }
        if(amount <= 0) {
            throw InvalidAmountError.of("잘못된 금액 형식입니다.");
        }
    }
}
