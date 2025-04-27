package kr.hhplus.be.server.presentation.coupon;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.application.coupon.IssueCouponCommand;

public record IssueCouponRequest(
        @Schema(description = "사용자 ID")
        Long userId,
        @Schema(description = "쿠폰 ID")
        Long couponId
) {
    public IssueCouponCommand toCommand() {
        return new IssueCouponCommand(userId, couponId);
    }
}
