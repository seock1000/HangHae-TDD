package kr.hhplus.be.server.presentation.point;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.application.point.ChargePointCommand;

public record ChargePointRequest(
        @Schema(description = "사용자 ID")
        Long userId,
        @Schema(description = "충전 금액")
        Integer amount
){
        public ChargePointCommand toCommand() {
                return new ChargePointCommand(
                        userId,
                        amount
                );
        }
}
