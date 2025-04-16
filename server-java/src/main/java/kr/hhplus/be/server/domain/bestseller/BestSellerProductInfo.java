package kr.hhplus.be.server.domain.bestseller;

import lombok.Getter;

@Getter
public class BestSellerProductInfo {
    private Long productId;
    private String title;
    private String description;
    private int stock;
    private int salesAmount;

    public BestSellerProductInfo(Long productId, String title, String description, int stock, int salesAmount) {
        this.productId = productId;
        this.title = title;
        this.description = description;
        this.stock = stock;
        this.salesAmount = salesAmount;
    }
}
