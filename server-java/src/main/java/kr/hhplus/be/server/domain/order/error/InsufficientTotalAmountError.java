package kr.hhplus.be.server.domain.order.error;

public class InsufficientTotalAmountError extends RuntimeException {
    public static InsufficientTotalAmountError of(String message) {
        return new InsufficientTotalAmountError(message);
    }

    private InsufficientTotalAmountError(String message) {
        super(message);
    }
}
