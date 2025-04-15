package kr.hhplus.be.server.domain.product;

public class SoldProduct {
    private Product product;

    private SoldProduct(Product product) {
        this.product = product;
    }

    protected static SoldProduct of(Product product) {
        return new SoldProduct(product);
    }

    public void deductStock(int amount) {
        this.product.decreaseStock(amount);
    }

    public Long getId() {
        return product.getId();
    }

    public Integer getPrice() {
        return product.getPrice();
    }
}
