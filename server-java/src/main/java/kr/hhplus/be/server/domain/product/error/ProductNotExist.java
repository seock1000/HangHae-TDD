package kr.hhplus.be.server.domain.product.error;

public class ProductNotExist extends RuntimeException {
    public static ProductNotExist of(String message) {
        return new ProductNotExist(message);
    }

    private ProductNotExist(String message) {
        super(message);
    }
}
