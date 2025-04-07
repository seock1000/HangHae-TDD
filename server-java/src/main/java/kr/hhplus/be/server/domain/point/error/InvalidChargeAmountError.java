package kr.hhplus.be.server.domain.point.error;

public class InvalidChargeAmountError extends RuntimeException {
    public static InvalidChargeAmountError of(String message) {
        return new InvalidChargeAmountError(message);
    }

    private InvalidChargeAmountError(String message) {
        super(message);
    }
}
