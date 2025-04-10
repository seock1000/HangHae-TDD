package kr.hhplus.be.server.domain.order.command;

/**
 * TC
 * null 여부는 controller에서 검증
 */
public record CancelOrderCommand(
        String orderId
) {
}
