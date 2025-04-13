package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.domain.order.Orders;

public record PayResult(
        String orderId
) {
    public static PayResult of(Orders order) {
        return new PayResult(order.getId());
    }
}
