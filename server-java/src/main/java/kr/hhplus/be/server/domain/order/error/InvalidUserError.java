package kr.hhplus.be.server.domain.order.error;

public class InvalidUserError extends RuntimeException {
    public static InvalidUserError of(String message) {
        return new InvalidUserError(message);
    }

    private InvalidUserError(String message) {
        super(message);
    }
}
