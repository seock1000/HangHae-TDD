package kr.hhplus.be.server.domain.product.error;

public class ProductNotExistError extends RuntimeException {
    public static ProductNotExistError of(String message) {
        return new ProductNotExistError(message);
    }

    private ProductNotExistError(String message) {
        super(message);
    }
}
