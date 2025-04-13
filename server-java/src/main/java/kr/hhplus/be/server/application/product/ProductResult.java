package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.domain.product.Product;

public record ProductResult(
        long productId,
        String title,
        String description,
        int price,
        int stock
) {
    public static ProductResult of(
            Product product
    ) {
        return new ProductResult(
                product.getId(),
                product.getTitle(),
                product.getDescription(),
                product.getPrice(),
                product.getStock()
        );
    }
}
