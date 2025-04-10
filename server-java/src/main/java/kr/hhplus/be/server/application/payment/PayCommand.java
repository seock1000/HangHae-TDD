package kr.hhplus.be.server.application.payment;

public record PayCommand(
        String orderId
) {
    public PayCommand {
        if (orderId == null || orderId.isBlank()) {
            throw InvalidOrderIdError.of("주문 ID는 필수입니다.");
        }
    }
}
