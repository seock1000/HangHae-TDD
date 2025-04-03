package kr.hhplus.be.server.coupon.controller.in;

import io.swagger.v3.oas.annotations.media.Schema;

public record IssueCouponRequest(
        @Schema(description = "사용자 ID")
        Long userId,
        @Schema(description = "쿠폰 ID")
        Long couponId
) {
}
