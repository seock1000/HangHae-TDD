package kr.hhplus.be.server.domain.product.command;

import kr.hhplus.be.server.domain.product.error.InvalidAmountError;
import kr.hhplus.be.server.domain.product.error.InvalidProductError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DecreaseStockCommandTest {

    @Test
    @DisplayName("DecreaseStockCommand 생성 시, 상품 ID와 수량이 0보다 크면 객체가 생성된다.")
    void testDecreaseStockCommand() {
        // given
        long productId = 1L;
        int amount = 10;

        // when
        DecreaseStockCommand command = new DecreaseStockCommand(productId, amount);

        // then
        assertEquals(productId, command.productId());
        assertEquals(amount, command.amount());
    }

    @Test
    @DisplayName("DecreaseStockCommand 생성 시, 상품 ID가 0 이하이면 InvalidProductError 예외가 발생한다.")
    void testInvalidProductId() {
        // given
        long invalidProductId = 0L;
        int amount = 10;

        // when, then
        Exception exception = assertThrows(InvalidProductError.class, () -> {
            new DecreaseStockCommand(invalidProductId, amount);
        });
        assertEquals("잘못된 상품 ID 형식입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("DecreaseStockCommand 생성 시, 수량이 0 이하이면 InvalidAmountError 예외가 발생한다.")
    void testInvalidAmount() {
        // given
        long productId = 1L;
        int invalidAmount = 0;

        // when, then
        Exception exception = assertThrows(InvalidAmountError.class, () -> {
            new DecreaseStockCommand(productId, invalidAmount);
        });
        assertEquals("잘못된 수량 형식입니다.", exception.getMessage());
    }

}