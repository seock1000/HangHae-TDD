package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.domain.product.error.InsufficientStockError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    @DisplayName("재고 증가시 amount만큼 재고가 증가한다.")
    public void increaseStock() {
        // given
        int givenStock = 100;
        int amount = 50;
        Product givenProduct = new Product(1L, "상품1", "상품1_desc", 1000, givenStock);
        Product expectedProduct = new Product(1L, "상품1", "상품1_desc", 1000, givenStock + amount);

        // when
        givenProduct.increaseStock(amount);

        // then
        assertEquals(expectedProduct, givenProduct);
    }

    @Test
    @DisplayName("재고 감소시 amount만큼 재고가 감소한다.")
    public void decreaseStock() {
        // given
        int givenStock = 100;
        int amount = 50;
        Product givenProduct = new Product(1L, "상품1", "상품1_desc", 1000, givenStock);
        Product expectedProduct = new Product(1L, "상품1", "상품1_desc", 1000, givenStock - amount);

        // when
        givenProduct.decreaseStock(amount);

        // then
        assertEquals(expectedProduct, givenProduct);
    }

    @Test
    @DisplayName("재고 감소시 amount가 재고보다 많으면 InsufficientStockError가 발생한다.")
    public void decreaseStock_InsufficientStockError() {
        // given
        int givenStock = 100;
        int amount = 150;
        Product givenProduct = new Product(1L, "상품1", "상품1_desc", 1000, givenStock);

        // when, then
        assertThrows(InsufficientStockError.class, () -> {
            givenProduct.decreaseStock(amount);
        });
    }

}