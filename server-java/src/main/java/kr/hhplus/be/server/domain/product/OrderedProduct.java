package kr.hhplus.be.server.domain.product;

public class OrderedProduct {
    private Product product;

    private OrderedProduct(Product product) {
        this.product = product;
    }

    public static OrderedProduct of(Product product) {
        return new OrderedProduct(product);
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
