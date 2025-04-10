package kr.hhplus.be.server.domain.order.error;

public class CanNotCancelOrderError extends RuntimeException {
    public static CanNotCancelOrderError of(String message) {
        return new CanNotCancelOrderError(message);
    }

    private CanNotCancelOrderError(String message) {
        super(message);
    }
}
