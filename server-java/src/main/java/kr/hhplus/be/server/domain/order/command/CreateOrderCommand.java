package kr.hhplus.be.server.domain.order.command;

import kr.hhplus.be.server.domain.order.error.*;

import java.util.List;

public record CreateOrderCommand(
        long userId,
        List<OrderItemSpec> orderItemSpecs
) {
    public CreateOrderCommand {
        if (userId <= 0) {
            throw InvalidUserError.of("잘못된 사용자 ID 형식입니다.");
        }
        if (orderItemSpecs == null || orderItemSpecs.isEmpty()) {
            throw InvalidOrderItemError.of("주문 항목이 비어있습니다.");
        }
    }

    public record OrderItemSpec(
            long productId,
            int price,
            int quantity
    ) {
        public OrderItemSpec {
            if (productId <= 0) {
                throw InvalidProductError.of("잘못된 상품 ID 형식입니다.");
            }
            if (price <= 0) {
                throw InvalidPriceError.of("잘못된 가격 형식입니다.");
            }
            if (quantity <= 0) {
                throw InvalidQuantityError.of("잘못된 수량 형식입니다.");
            }
        }
    }
}
