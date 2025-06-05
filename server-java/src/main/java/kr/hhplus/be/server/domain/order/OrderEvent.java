package kr.hhplus.be.server.domain.order;

import java.time.LocalDate;
import java.util.List;

public class OrderEvent {

    public record Confirmed(
            String orderId,
            Long userId,
            Long CouponId,
            int totalAmount,
            int discountAmount,
            LocalDate orderDate,
            List<OrderedItem> items
    ) {
        public static Confirmed of(Orders order) {
            return new Confirmed(
                    order.getId(),
                    order.getUser(),
                    order.getCouponId(),
                    order.getTotalAmount(),
                    order.getDiscountAmount(),
                    order.getOrderDate(),
                    order.getOrderItems().stream()
                            .map(OrderedItem::of)
                            .toList()
            );
        }
    }

    public record OrderedItem(
            Long productId,
            int amount,
            int price,
            int quantity
    ) {
        public static OrderedItem of(OrderItem orderItem) {
            return new OrderedItem(
                    orderItem.getProductId(),
                    orderItem.getAmount(),
                    orderItem.getPrice(),
                    orderItem.getQuantity()
            );
        }
    }
}
