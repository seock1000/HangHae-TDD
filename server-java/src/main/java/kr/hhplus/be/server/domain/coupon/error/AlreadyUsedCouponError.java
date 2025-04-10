package kr.hhplus.be.server.domain.coupon.error;

public class AlreadyUsedCouponError extends RuntimeException {
    public static AlreadyUsedCouponError of(String message) {
        return new AlreadyUsedCouponError(message);
    }

    private AlreadyUsedCouponError(String message) {
        super(message);
    }
}
