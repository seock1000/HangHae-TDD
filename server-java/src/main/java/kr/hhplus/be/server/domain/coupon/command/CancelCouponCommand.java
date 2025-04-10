package kr.hhplus.be.server.domain.coupon.command;

import kr.hhplus.be.server.domain.coupon.error.InvalidAmountError;
import kr.hhplus.be.server.domain.coupon.error.InvalidCouponIdError;

public record CancelCouponCommand(
        long userCouponId
) {
    public CancelCouponCommand {
        if(userCouponId <= 0) {
            throw InvalidCouponIdError.of("잘못된 쿠폰 ID 형식입니다.");
        }
    }
}
