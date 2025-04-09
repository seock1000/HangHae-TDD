package kr.hhplus.be.server.domain.product.command;

import kr.hhplus.be.server.domain.product.error.InvalidAmountError;
import kr.hhplus.be.server.domain.product.error.InvalidProductError;

public record DecreaseStockCommand(
        long productId,
        int amount
) {
    public DecreaseStockCommand {
        if (productId <= 0) {
            throw InvalidProductError.of("잘못된 상품 ID 형식입니다.");
        }
        if (amount <= 0) {
            throw InvalidAmountError.of("잘못된 수량 형식입니다.");
        }
    }
}
