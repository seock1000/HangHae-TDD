package kr.hhplus.be.server.presentation.order;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateOrderProductRequest(
        @Schema(description = "상품 ID")
        Long productId,
        @Schema(description = "상품 수량")
        Integer quantity
) {
}
