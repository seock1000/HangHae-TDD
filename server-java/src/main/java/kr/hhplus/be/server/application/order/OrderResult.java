package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.Orders;

public record OrderResult(
        String orderId
) {
    public static OrderResult of(Orders order) {
        return new OrderResult(order.getId());
    }
}
