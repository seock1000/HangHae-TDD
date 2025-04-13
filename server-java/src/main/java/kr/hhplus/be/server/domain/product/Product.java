package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.ApiError;
import kr.hhplus.be.server.ApiException;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Product {

    private Long id;
    private String title;
    private String description;
    private int price;
    private int stock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * TC
     * 상품의 재고를 차감시킨다.
     * 재고가 부족할 경우 ApiException(INSUFFICIENT_PRODUCT_STOCK)을 발생시킨다.
     */
    public void decreaseStock(int quantity) {
        if (stock < quantity) {
            throw ApiException.of(ApiError.INSUFFICIENT_PRODUCT_STOCK);
        }
        this.stock -= quantity;
    }

    /**
     * TC
     * 상품의 재고를 증가시킨다.
     */
    public void increaseStock(int quantity) {
        this.stock += quantity;
    }
}
