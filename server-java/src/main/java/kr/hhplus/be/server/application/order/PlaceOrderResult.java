package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.Orders;

public record PlaceOrderResult(
        String orderId
) {
    public static PlaceOrderResult of(Orders order) {
        return new PlaceOrderResult(order.getId());
    }
}
