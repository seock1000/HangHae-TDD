package kr.hhplus.be.server.bestseller.controller.out;

public record GetBestSellerResponse(
        Long id,
        String name,
        Integer price,
        Integer sales,
        Integer stock
){
}
