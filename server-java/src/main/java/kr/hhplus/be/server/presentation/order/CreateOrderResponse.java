package kr.hhplus.be.server.presentation.order;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.application.order.OrderResult;

public record CreateOrderResponse(
        @Schema(description = "주문 ID")
        String orderId
) {
        public static CreateOrderResponse of(OrderResult result) {
                return new CreateOrderResponse(result.orderId());
        }
}
