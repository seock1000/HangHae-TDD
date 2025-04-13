package kr.hhplus.be.server.domain.order;

import jakarta.persistence.criteria.Order;
import kr.hhplus.be.server.domain.product.Product;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public class Orders {
    private String id;
    private Long user;
    private Optional<Long> couponId;
    private int totalAmount;
    private OrderStatus status;
    private List<OrderItem> orderItems;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Orders(String id, Long user, int totalAmount, OrderStatus status) {
        this.id = id;
        this.user = user;
        this.totalAmount = totalAmount;
        this.status = status;
        this.orderItems = new ArrayList<>();
    }

    public static Orders createWithIdAndUser(String id, Long user) {
        return new Orders(id, user, 0, OrderStatus.PENDING);
    }

    public void addProduct(Product product, int quantity) {
        product.decreaseStock(quantity);
        OrderItem orderItem = OrderItem.create(this.id, product, quantity);
        totalAmount += orderItem.getAmount();
        orderItems.add(orderItem);
    }
}
