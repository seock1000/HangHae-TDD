package kr.hhplus.be.server.application.payment;

import jakarta.persistence.criteria.Order;
import kr.hhplus.be.server.domain.order.Orders;
import lombok.Data;

@Data
public class OrderPaidEvent {
    private Orders order;

    private OrderPaidEvent(Orders order) {
        this.order = order;
    }

    public static OrderPaidEvent of(Orders order) {
        return new OrderPaidEvent(order);
    }
}
