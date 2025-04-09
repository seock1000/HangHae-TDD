package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.order.error.InsufficientTotalAmountError;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Orders {
    private String id;
    private Long user;
    private int totalAmount;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Orders(String id, Long user, int totalAmount, OrderStatus status) {
        this.id = id;
        this.user = user;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public static Orders createWithIdAndUser(String id, Long user) {
        return new Orders(id, user, 0, OrderStatus.PENDING);
    }

    public void plusTotalAmount(int amount) {
        this.totalAmount += amount;
    }

    public void minusTotalAmount(int amount) {
        if(this.totalAmount < amount) {
            throw InsufficientTotalAmountError.of("총 주문금액은 0원 이상이어야 합니다.");
        }
        this.totalAmount -= amount;
    }
}
