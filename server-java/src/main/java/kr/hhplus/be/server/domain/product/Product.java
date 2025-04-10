package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.domain.product.error.InsufficientStockError;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
public class Product {
    private Long id;
    private String title;
    private String description;
    private int price;
    private int stock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Product(Long id, String title, String description, int price, int stock) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    /**
     * TC
     * 입력받은 amount 만큼 재고를 증가시킨다.
     */
    public void increaseStock(int amount) {
        this.stock += amount;
    }

    /**
     * TC
     * 입력받은 amount 만큼 재고를 감소시킨다.
     * 재고 차감 시 재고가 부족하면 실패한다. => InsufficientStockError
     */
    public void decreaseStock(int amount) {
        if (this.stock < amount) {
            throw InsufficientStockError.of("재고가 부족합니다.");
        }
        this.stock -= amount;
    }
}
