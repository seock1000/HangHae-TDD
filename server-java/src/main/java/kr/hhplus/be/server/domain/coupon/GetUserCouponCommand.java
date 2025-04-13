package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.ApiError;
import kr.hhplus.be.server.ApiException;

public record GetUserCouponCommand(
        long userId
) {
    public GetUserCouponCommand {
        if (userId <= 0) {
            throw ApiException.of(ApiError.INVALID_USER_ID);
        }
    }
}
