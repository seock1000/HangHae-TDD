package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.product.Product;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderItem {
    private Long id;
    private String ordersId;
    private Long productId;
    private int amount;
    private int price;
    private int quantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private OrderItem(String ordersId, Long productId, int amount, int price, int quantity) {
        this.ordersId = ordersId;
        this.productId = productId;
        this.amount = amount;
        this.price = price;
        this.quantity = quantity;
    }

    public static OrderItem create(String ordersId, Product product, int quantity) {
        return new OrderItem(ordersId, product.getId(), product.getPrice() * quantity, product.getPrice(), quantity);
    }
}
