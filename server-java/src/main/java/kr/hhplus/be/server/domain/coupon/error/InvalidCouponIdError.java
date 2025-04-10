package kr.hhplus.be.server.domain.coupon.error;

public class InvalidCouponIdError extends RuntimeException {
    public static InvalidCouponIdError of(String message) {
        return new InvalidCouponIdError(message);
    }

    private InvalidCouponIdError(String message) {
        super(message);
    }
}
