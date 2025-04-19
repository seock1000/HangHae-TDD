package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.domain.order.Orders;
import kr.hhplus.be.server.domain.payment.Payment;

public record PayResult(
        Long paymentId
) {
    public static PayResult of(Payment payment) {
        return new PayResult(payment.getId());
    }
}
