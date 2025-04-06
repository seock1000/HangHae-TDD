package kr.hhplus.be.server.point.controller.out;

import io.swagger.v3.oas.annotations.media.Schema;

public record ChargePointResponse(
        @Schema(description = "사용자 ID")
        Long userId,
        @Schema(description = "충전 금액")
        Integer balance
) {
}
