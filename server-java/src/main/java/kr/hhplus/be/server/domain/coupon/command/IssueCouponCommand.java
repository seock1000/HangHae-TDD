package kr.hhplus.be.server.domain.coupon.command;

import kr.hhplus.be.server.domain.coupon.error.InvalidCouponIdError;
import kr.hhplus.be.server.domain.coupon.error.InvalidUserIdError;

public record IssueCouponCommand(
        long couponId,
        long userId
) {
    public IssueCouponCommand {
        if(couponId <= 0) {
            throw InvalidCouponIdError.of("잘못된 쿠폰 ID 형식입니다.");
        }
        if(userId <= 0) {
            throw InvalidUserIdError.of("잘못된 사용자 ID 형식입니다.");
        }
    }
}
