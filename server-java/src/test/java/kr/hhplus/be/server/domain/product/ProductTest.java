package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.ApiError;
import kr.hhplus.be.server.ApiException;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    @DisplayName("상품 재고 차감시, 입력 값만큼 상품 재고가 감소한다.")
    void reduceStock() {
        // given
        Product product = Instancio.of(Product.class)
                .set(field("stock"), 10)
                .create();

        // when
        product.decreaseStock(5);

        // then
        assertEquals(5, product.getStock());
    }

    @Test
    @DisplayName("상품 재고 차감시, 재고가 부족할 경우 ApiException(INSUFFICIENT_PRODUCT_STOCK)이 발생한다.")
    void reduceStock_InsufficientStock() {
        // given
        Product product = Instancio.of(Product.class)
                .set(field("stock"), 5)
                .create();

        // when
        ApiException exception = assertThrows(ApiException.class, () -> product.decreaseStock(10));

        // then
        assertEquals(ApiError.INSUFFICIENT_PRODUCT_STOCK, exception.getApiError());
    }


}