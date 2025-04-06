package kr.hhplus.be.server.presentation.order;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateOrderResponse(
        @Schema(description = "주문 ID")
        Long orderId
) {
}
