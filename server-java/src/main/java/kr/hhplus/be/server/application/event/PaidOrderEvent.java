package kr.hhplus.be.server.application.event;

import kr.hhplus.be.server.domain.bestseller.SalesStat;
import kr.hhplus.be.server.domain.order.OrderData;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.Orders;

import java.time.LocalDate;
import java.util.List;

public record PaidOrderEvent(
        String orderId,
        Long userId,
        Long couponId,
        int totalAmount,
        int discountAmount,
        LocalDate orderDate,
        List<PaidOrderItem> orderItems
) {
    public static PaidOrderEvent of(Orders orders) {
        return new PaidOrderEvent(
                orders.getId(),
                orders.getUser(),
                orders.getCouponId(),
                orders.getTotalAmount(),
                orders.getDiscountAmount(),
                orders.getOrderDate(),
                orders.getOrderItems().stream()
                        .map(PaidOrderItem::of)
                        .toList()
        );
    }

    public List<OrderData> toOrderData() {
        return orderItems.stream()
                .map(it -> new OrderData(userId, it.productId(), it.amount(), it.quantity()))
                .toList();
    }

    public record PaidOrderItem(
            Long productId,
            int amount,
            int price,
            int quantity
    ) {
        public static PaidOrderItem of(OrderItem orderItem) {
            return new PaidOrderItem(
                    orderItem.getProductId(),
                    orderItem.getAmount(),
                    orderItem.getPrice(),
                    orderItem.getQuantity()
            );
        }

        public SalesStat toSalesStat() {
            return new SalesStat(productId, (long) quantity);
        }
    }
}
