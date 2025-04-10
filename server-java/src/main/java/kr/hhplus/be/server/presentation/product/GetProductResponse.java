package kr.hhplus.be.server.presentation.product;

import io.swagger.v3.oas.annotations.media.Schema;

public record GetProductResponse(
        @Schema(description = "상품 ID")
        Long id,
        @Schema(description = "상품 이름")
        String name,
        @Schema(description = "상품 가격")
        Integer price,
        @Schema(description = "상품 재고")
        Integer stock
){
}
