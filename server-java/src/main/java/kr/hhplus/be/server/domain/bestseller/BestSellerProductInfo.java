package kr.hhplus.be.server.domain.bestseller;

import lombok.Getter;

@Getter
public class BestSellerProductInfo {
    private Long productId;
    private String title;
    private String description;
    private String stock;
    private int salesAmount;
}
