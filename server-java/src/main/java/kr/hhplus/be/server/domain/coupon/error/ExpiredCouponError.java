package kr.hhplus.be.server.domain.coupon.error;

public class ExpiredCouponError extends RuntimeException {
    public static ExpiredCouponError of(String message) {
        return new ExpiredCouponError(message);
    }

    private ExpiredCouponError(String message) {
        super(message);
    }
}
