package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.UserCoupon;

public record IssueCouponResult(
        long userCouponId
) {
    public static IssueCouponResult of(UserCoupon userCoupon) {
        return new IssueCouponResult(userCoupon.getId());
    }
}
