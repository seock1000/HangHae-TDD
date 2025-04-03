package kr.hhplus.be.server.product.controller.out;

public record GetProductResponse(
        Long id,
        String name,
        Integer price,
        Integer stock
){
}
