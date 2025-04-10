package kr.hhplus.be.server.domain.order.error;

public class InvalidProductError extends RuntimeException {
    public static InvalidProductError of(String message) {
        return new InvalidProductError(message);
    }

    private InvalidProductError(String message) {
        super(message);
    }
}
