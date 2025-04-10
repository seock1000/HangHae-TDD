package kr.hhplus.be.server.domain.order.error;

public class InvalidPriceError extends RuntimeException {
    public static InvalidPriceError of(String message) {
        return new InvalidPriceError(message);
    }

    private InvalidPriceError(String message) {
        super(message);
    }
}
