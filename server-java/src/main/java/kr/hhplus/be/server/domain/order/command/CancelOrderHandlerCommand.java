package kr.hhplus.be.server.domain.order.command;

import kr.hhplus.be.server.domain.order.error.InvalidOrderIdError;

/**
 * TC
 * null 여부는 controller에서 검증
 */
public record CancelOrderHandlerCommand(
        String orderId
) {
    public CancelOrderHandlerCommand {
        if (orderId == null || orderId.isBlank()) {
            throw InvalidOrderIdError.of("주문번호는 필수입니다.");
        }
    }
}
