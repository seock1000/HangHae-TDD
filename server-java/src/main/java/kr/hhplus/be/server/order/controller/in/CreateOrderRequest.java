package kr.hhplus.be.server.order.controller.in;

import java.util.List;

public record CreateOrderRequest(
        Long userId,
        Long userCouponId,
        List<CreateOrderProductRequest> orderProducts
) {
}
