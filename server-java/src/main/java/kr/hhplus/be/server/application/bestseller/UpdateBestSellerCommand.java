package kr.hhplus.be.server.application.bestseller;

public record UpdateBestSellerCommand(
        String orderId
) {
    public static UpdateBestSellerCommand of(String orderId) {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("Order ID cannot be null or blank");
        }
        return new UpdateBestSellerCommand(orderId);
    }
}
