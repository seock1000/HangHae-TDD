package kr.hhplus.be.server.presentation.product;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.application.product.ProductResult;

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
        public static GetProductResponse of(ProductResult result) {
                return new GetProductResponse(
                        result.productId(),
                        result.title(),
                        result.price(),
                        result.stock()
                );
        }
}
