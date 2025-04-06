package kr.hhplus.be.server.order.controller.in;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateOrderProductRequest(
        @Schema(description = "상품 ID")
        Long productId,
        @Schema(description = "상품 수량")
        Integer quantity
) {
}
