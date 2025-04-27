package kr.hhplus.be.server.presentation.point;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.application.point.PointResult;

public record ChargePointResponse(
        @Schema(description = "포인트 ID")
        Long pointId,
        @Schema(description = "충전 금액")
        Integer balance
) {
        public static ChargePointResponse of(PointResult result) {
                return new ChargePointResponse(
                        result.pointId(),
                        result.balance()
                );
        }
}
