package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.Orders;

public record OrderResult(
        String orderId
) {
    public OrderResult of(Orders orders) {
        return new OrderResult(orders.getId());
    }
}
