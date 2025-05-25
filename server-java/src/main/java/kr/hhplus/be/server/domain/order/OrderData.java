package kr.hhplus.be.server.domain.order;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderData {
    private Long userId;
    private Long productId;
    private Integer quantity;
    private Integer price;

    public OrderData(Long userId, Long productId, Integer quantity, Integer price) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public static List<OrderData> of(Orders order) {
        return order.getOrderItems().stream()
                .map(orderItem -> new OrderData(
                        order.getUser(),
                        orderItem.getProductId(),
                        orderItem.getQuantity(),
                        orderItem.getPrice()))
                .toList();
    }
}
