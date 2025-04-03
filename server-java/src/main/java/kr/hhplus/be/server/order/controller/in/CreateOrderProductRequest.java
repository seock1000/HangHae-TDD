package kr.hhplus.be.server.order.controller.in;

public record CreateOrderProductRequest(
        Long productId,
        Integer quantity
) {
}
