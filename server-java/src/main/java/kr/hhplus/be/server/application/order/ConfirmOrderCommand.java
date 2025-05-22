package kr.hhplus.be.server.application.order;

public record ConfirmOrderCommand(
        String orderId
) {
    public static ConfirmOrderCommand of(String orderId) {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("Order ID cannot be null or blank");
        }
        return new ConfirmOrderCommand(orderId);
    }
}
