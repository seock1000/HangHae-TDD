package kr.hhplus.be.server.application.order.error;

public class InvalidQuantityError extends RuntimeException {
    public static InvalidQuantityError of(String message) {
        return new InvalidQuantityError(message);
    }

    private InvalidQuantityError(String message) {
        super(message);
    }
}
