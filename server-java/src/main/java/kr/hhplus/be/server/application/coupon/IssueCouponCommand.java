package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.ApiError;
import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.domain.coupon.CouponEvent;

public record IssueCouponCommand(
        long userId,
        long couponId
) {
    public IssueCouponCommand {
        if (userId <= 0) {
            throw ApiException.of(ApiError.INVALID_USER_ID);
        }
        if (couponId <= 0) {
            throw ApiException.of(ApiError.INVALID_COUPON_ID);
        }
    }

    public CouponEvent.Issue toEvent() {
        return new CouponEvent.Issue(this.userId, this.couponId);
    }
}
