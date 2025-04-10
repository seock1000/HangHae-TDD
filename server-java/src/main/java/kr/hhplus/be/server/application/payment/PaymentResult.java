package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.domain.order.Orders;

public record PaymentResult(
        String orderId
) {
    public static PaymentResult of(Orders order) {
        return new PaymentResult(order.getId());
    }
}
