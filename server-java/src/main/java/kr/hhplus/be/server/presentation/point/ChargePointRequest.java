package kr.hhplus.be.server.presentation.point;

import io.swagger.v3.oas.annotations.media.Schema;

public record ChargePointRequest(
        @Schema(description = "사용자 ID")
        Long userId,
        @Schema(description = "충전 금액")
        Integer amount
){
}
