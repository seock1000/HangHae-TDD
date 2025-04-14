package kr.hhplus.be.server.application.point;

import kr.hhplus.be.server.ApiError;
import kr.hhplus.be.server.ApiException;

public record ChargePointCommand(
        long userId,
        int amount
) {
    public ChargePointCommand {
        if (userId <= 0) {
            throw ApiException.of(ApiError.INVALID_USER_ID);
        }
        if (amount <= 0) {
            throw ApiException.of(ApiError.INVALID_CHARGE_AMOUNT);
        }
    }
}
