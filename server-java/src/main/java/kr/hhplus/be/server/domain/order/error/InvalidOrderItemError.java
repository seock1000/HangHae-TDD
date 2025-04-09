package kr.hhplus.be.server.domain.order.error;

public class InvalidOrderItemError extends RuntimeException {
    public static InvalidOrderItemError of(String message) {
        return new InvalidOrderItemError(message);
    }

    private InvalidOrderItemError(String message) {
        super(message);
    }
}
