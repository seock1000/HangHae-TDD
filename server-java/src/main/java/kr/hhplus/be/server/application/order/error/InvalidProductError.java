package kr.hhplus.be.server.application.order.error;

public class InvalidProductError extends RuntimeException {
    public static InvalidProductError of(String message) {
        return new InvalidProductError(message);
    }

    private InvalidProductError(String message) {
        super(message);
    }
}
