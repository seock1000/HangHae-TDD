package kr.hhplus.be.server.presentation.point;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.application.point.PointResult;

public record GetPointResponse(
        @Schema(description = "포인트 ID")
        Long pointId,
        @Schema(description = "포인트 잔액")
        Integer balance
) {
        public static GetPointResponse of(PointResult result) {
                return new GetPointResponse(
                        result.pointId(),
                        result.balance()
                );
        }
}
