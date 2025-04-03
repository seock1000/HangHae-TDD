package kr.hhplus.be.server.point.controller.out;

import io.swagger.v3.oas.annotations.media.Schema;

public record GetPointResponse(
        @Schema(description = "사용자 ID")
        Long userId,
        @Schema(description = "포인트 잔액")
        Integer balance
) {
}
