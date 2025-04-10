package kr.hhplus.be.server.domain.order.error;

public class CanNotConfirmOrderError extends RuntimeException {
    public static CanNotConfirmOrderError of(String message) {
        return new CanNotConfirmOrderError(message);
    }

    private CanNotConfirmOrderError(String message) {
        super(message);
    }
}
