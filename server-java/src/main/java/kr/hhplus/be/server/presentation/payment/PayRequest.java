package kr.hhplus.be.server.presentation.payment;

import io.swagger.v3.oas.annotations.media.Schema;

public record PayRequest(
        @Schema(description = "주문 ID")
        Long orderId
) {
}
