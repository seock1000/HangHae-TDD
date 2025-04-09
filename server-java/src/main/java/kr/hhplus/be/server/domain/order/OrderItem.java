package kr.hhplus.be.server.domain.order;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderItem {
    Long id;
    String ordersId;
    Long productId;
    int amount;
    int price;
    int quantity;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    private OrderItem(String ordersId, Long productId, int amount, int price, int quantity) {
        this.ordersId = ordersId;
        this.productId = productId;
        this.amount = amount;
        this.price = price;
        this.quantity = quantity;
    }

    public static OrderItem create(String ordersId, Long productId, int price, int quantity) {
        return new OrderItem(ordersId, productId, price * quantity, price, quantity);
    }
}
