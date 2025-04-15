package kr.hhplus.be.server.domain.payment;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.hhplus.be.server.config.jpa.BaseTimeEntity;
import kr.hhplus.be.server.domain.order.PaidOrder;
import kr.hhplus.be.server.domain.point.UsedPoint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderId;
    private PaymentMethod method;
    private int amount;

    private Payment(String orderId, PaymentMethod method, int amount) {
        this.orderId = orderId;
        this.method = method;
        this.amount = amount;
    }

    public static Payment payWithPoint(PaidOrder order, UsedPoint usedPoint) {
        usedPoint.use(order.getTotalAmount());
        order.confirm();
        return new Payment(order.getId(), PaymentMethod.POINT, order.getTotalAmount());
    }
}
