package kr.hhplus.be.server.presentation.bestseller;

import io.swagger.v3.oas.annotations.media.Schema;

public record GetBestSellerResponse(
        @Schema(description = "상품 ID")
        Long id,
        @Schema(description = "상품 이름")
        String name,
        @Schema(description = "상품 가격")
        Integer price,
        @Schema(description = "상품 판매량")
        Integer sales,
        @Schema(description = "상품 재고")
        Integer stock
){
}
