package kr.hhplus.be.server.application.order;

import java.util.List;

public record OrderCriteria(
        long userId,
        List<OrderItemSpec> orderItemSpecs
) {
    record OrderItemSpec(
            long productId,
            int quantity
    ) {
    }
}
