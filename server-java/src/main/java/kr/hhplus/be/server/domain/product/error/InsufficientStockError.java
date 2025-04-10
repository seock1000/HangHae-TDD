package kr.hhplus.be.server.domain.product.error;

public class InsufficientStockError extends RuntimeException {
    public static InsufficientStockError of(String message) {
        return new InsufficientStockError(message);
    }

    private InsufficientStockError(String message) {
        super(message);
    }
}
