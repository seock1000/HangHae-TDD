package kr.hhplus.be.server.domain.product;

public class RefundedProduct {
    private Product product;

    private RefundedProduct(Product product) {
        this.product = product;
    }

    protected static RefundedProduct of(Product product) {
        return new RefundedProduct(product);
    }

    public void refundStock(int amount) {
        this.product.increaseStock(amount);
    }
}
