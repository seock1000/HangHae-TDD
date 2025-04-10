package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.order.error.CanNotCancelOrderError;
import kr.hhplus.be.server.domain.order.error.CanNotConfirmOrderError;
import kr.hhplus.be.server.domain.order.error.InsufficientTotalAmountError;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Orders {
    private String id;
    private Long user;
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

    public void addOrderItem(OrderItem orderItem) {
        this.totalAmount += orderItem.getAmount();
        this.orderItems.add(orderItem);
    }

    public void minusTotalAmount(int amount) {
        if(this.totalAmount < amount) {
            throw InsufficientTotalAmountError.of("총 주문금액은 0원 이상이어야 합니다.");
        }
        this.totalAmount -= amount;
    }

    public void cancel() {
        if (this.status != OrderStatus.PENDING) {
            throw CanNotCancelOrderError.of("주문을 취소할 수 없습니다.");
        }
        this.status = OrderStatus.CANCELLED;
    }

    public void confirm() {
        if (this.status != OrderStatus.PENDING) {
            throw CanNotConfirmOrderError.of("주문을 확정할 수 없습니다.");
        }
        this.status = OrderStatus.CONFIRMED;
    }
}
