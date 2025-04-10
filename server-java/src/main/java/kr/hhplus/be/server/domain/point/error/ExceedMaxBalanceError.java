package kr.hhplus.be.server.domain.point.error;

public class ExceedMaxBalanceError extends RuntimeException {
    public static ExceedMaxBalanceError of(String message) {
        return new ExceedMaxBalanceError(message);
    }

    private ExceedMaxBalanceError(String message) {
        super(message);
    }
}
