package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.domain.order.Orders;
import lombok.Data;
import lombok.Getter;

@Getter
public class PaymentEvent {
    private Long paymentId;
    private String orderId;
    private PaymentMethod method;
    private int amount;

    private PaymentEvent(Long paymentId, String orderId, PaymentMethod method, int amount) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.method = method;
        this.amount = amount;
    }

    public static PaymentEvent of(Payment payment) {
        return new PaymentEvent(
                payment.getId(),
                payment.getOrderId(),
                payment.getMethod(),
                payment.getAmount()
        );
    }
}
