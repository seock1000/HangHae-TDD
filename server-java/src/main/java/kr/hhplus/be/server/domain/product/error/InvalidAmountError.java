package kr.hhplus.be.server.domain.product.error;

public class InvalidAmountError extends RuntimeException {
    public static InvalidAmountError of(String message) {
        return new InvalidAmountError(message);
    }

    private InvalidAmountError(String message) {
        super(message);
    }
}
