package kr.hhplus.be.server.domain.point.error;

public class InsufficientBalanceError extends RuntimeException {
    public static InsufficientBalanceError of(String message) {
        return new InsufficientBalanceError(message);
    }

    private InsufficientBalanceError(String message) {
        super(message);
    }
}
