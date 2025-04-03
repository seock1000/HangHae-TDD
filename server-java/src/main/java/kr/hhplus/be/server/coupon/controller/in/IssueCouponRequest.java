package kr.hhplus.be.server.coupon.controller.in;

public record IssueCouponRequest(
        Long userId,
        Long couponId
) {
}
