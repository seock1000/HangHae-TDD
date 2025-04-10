package kr.hhplus.be.server.domain.coupon.error;

public class CouponNotExistError extends RuntimeException {
    public static CouponNotExistError of(String message) {
        return new CouponNotExistError(message);
    }

    private CouponNotExistError(String message) {
        super(message);
    }
}
