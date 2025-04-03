package kr.hhplus.be.server.order.controller.out;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateOrderResponse(
        @Schema(description = "주문 ID")
        Long orderId
) {
}
