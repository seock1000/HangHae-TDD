package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.application.event.PaidOrderEvent;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.domain.point.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentFacade {
    private final OrderService orderService;
    private final PointService pointService;
    private final PaymentService paymentService;

    private final ApplicationEventPublisher eventPublisher;

    public PayResult pay(PayCommand command) {
        var order = orderService.getOrderById(command.orderId());
        var point = pointService.getPointByUserId(order.getUser());

        Payment payment = Payment.payWithPoint(order.toPaidOrder(), point.toUserPoint());

        pointService.save(point);
        orderService.saveOrder(order);
        paymentService.savePayment(payment);


        orderService.removeOrderToCancelHandler(order);
        eventPublisher.publishEvent(PaidOrderEvent.of(order));
        return PayResult.of(payment);
    }
}
