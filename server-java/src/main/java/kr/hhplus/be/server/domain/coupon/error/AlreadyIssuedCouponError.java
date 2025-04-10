package kr.hhplus.be.server.domain.coupon.error;

public class AlreadyIssuedCouponError extends RuntimeException {
    public static AlreadyIssuedCouponError of(String message) {
        return new AlreadyIssuedCouponError(message);
    }

    private AlreadyIssuedCouponError(String message) {
        super(message);
    }
}
