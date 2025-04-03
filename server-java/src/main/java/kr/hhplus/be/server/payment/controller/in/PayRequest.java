package kr.hhplus.be.server.payment.controller.in;

import io.swagger.v3.oas.annotations.media.Schema;

public record PayRequest(
        @Schema(description = "주문 ID")
        Long orderId
) {
}
