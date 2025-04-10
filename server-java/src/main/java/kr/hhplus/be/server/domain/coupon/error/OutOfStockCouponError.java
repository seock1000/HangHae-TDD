package kr.hhplus.be.server.domain.coupon.error;

public class OutOfStockCouponError extends RuntimeException {
    public static OutOfStockCouponError of(String message) {
        return new OutOfStockCouponError(message);
    }

    private OutOfStockCouponError(String message) {
        super(message);
    }
}
