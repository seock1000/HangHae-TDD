package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.ApiError;
import kr.hhplus.be.server.ApiException;

public record CancelOrderCommand(
        String orderId
) {
    public CancelOrderCommand {
        if(orderId == null || orderId.isBlank()) {
            throw ApiException.of(ApiError.INVALID_ORDER_ID);
        }
    }
}
