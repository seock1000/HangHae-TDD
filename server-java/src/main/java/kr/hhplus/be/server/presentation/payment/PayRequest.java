package kr.hhplus.be.server.presentation.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.application.payment.PayCommand;

public record PayRequest(
        @Schema(description = "주문 ID")
        String orderId
) {
        public PayCommand toCommand() {
                return new PayCommand(orderId);
        }
}
