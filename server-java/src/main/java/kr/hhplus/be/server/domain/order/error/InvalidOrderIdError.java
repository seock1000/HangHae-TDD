package kr.hhplus.be.server.domain.order.error;

public class InvalidOrderIdError extends RuntimeException {
    public static InvalidOrderIdError of(String message) {
        return new InvalidOrderIdError(message);
    }

    private InvalidOrderIdError(String message) {
        super(message);
    }
}
