package kr.hhplus.be.server.domain.product;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.hhplus.be.server.ApiError;
import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.config.jpa.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private int price;
    private int stock;

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
