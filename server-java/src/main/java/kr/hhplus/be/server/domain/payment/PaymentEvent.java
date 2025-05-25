package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.domain.order.Orders;
import lombok.Data;
import lombok.Getter;

@Getter
public class PaymentEvent {

    public record Completed(
            Long paymentId,
            String orderId,
            PaymentMethod method,
            int amount
    ) {
        public static Completed of(Payment payment) {
            return new Completed(
                    payment.getId(),
                    payment.getOrderId(),
                    payment.getMethod(),
                    payment.getAmount()
            );
        }
    }
}
