package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.order.error.InvalidProductError;
import kr.hhplus.be.server.application.order.error.InvalidQuantityError;
import kr.hhplus.be.server.application.order.error.InvalidUserIdError;

import java.util.List;

public record PlaceOrderCommand(
        long userId,
        List<OrderItemSpec> orderItemSpecs
) {
    public PlaceOrderCommand {
        if (userId <= 0) {
            throw InvalidUserIdError.of("잘못된 사용자 ID 형식입니다.");
        }
        if (orderItemSpecs == null || orderItemSpecs.isEmpty()) {
            throw InvalidUserIdError.of("주문 항목이 비어있습니다.");
        }
    }
    record OrderItemSpec(
            long productId,
            int quantity
    ) {
        public OrderItemSpec {
            if (productId <= 0) {
                throw InvalidProductError.of("잘못된 상품 ID 형식입니다.");
            }
            if (quantity <= 0) {
                throw InvalidQuantityError.of("잘못된 수량 형식입니다.");
            }
        }
    }
}
