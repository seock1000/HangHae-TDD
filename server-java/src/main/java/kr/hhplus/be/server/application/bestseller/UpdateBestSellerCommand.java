package kr.hhplus.be.server.application.bestseller;

import kr.hhplus.be.server.domain.bestseller.SalesStat;

public record UpdateBestSellerCommand(
        Long productId,
        int quantity
) {

    public SalesStat toSalesStat() {
        return new SalesStat(productId, (long) quantity);
    }
}
