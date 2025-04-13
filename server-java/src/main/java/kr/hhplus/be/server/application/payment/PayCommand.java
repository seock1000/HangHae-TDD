package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.ApiError;
import kr.hhplus.be.server.ApiException;

public record PayCommand(
        String orderId
) {
    public PayCommand {
        if(orderId == null || orderId.isBlank()) {
            throw ApiException.of(ApiError.INVALID_ORDER_ID);
        }
    }
}
