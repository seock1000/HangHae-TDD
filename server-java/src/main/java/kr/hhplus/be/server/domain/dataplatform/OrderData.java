package kr.hhplus.be.server.domain.dataplatform;

public record OrderData(
        Long userId,
        Long productId,
        Integer quantity,
        Integer price
) {
}
