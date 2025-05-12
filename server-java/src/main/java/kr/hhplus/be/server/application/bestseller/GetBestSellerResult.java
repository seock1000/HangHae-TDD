package kr.hhplus.be.server.application.bestseller;

import kr.hhplus.be.server.domain.bestseller.BestSeller;
import kr.hhplus.be.server.domain.product.Product;

public record GetBestSellerResult(
        Long productId,
        String title,
        String description,
        int price,
        int stock,
        int salesAmount
) {
    public static GetBestSellerResult of(BestSeller bestSeller, Product product) {
        return new GetBestSellerResult(
                bestSeller.getProductId(),
                product.getTitle(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                bestSeller.getSalesAmount()
        );
    }
}
