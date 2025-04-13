package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.point.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentFacade {
    private final OrderService orderService;
    private final PointService pointService;

    public PayResult pay(PayCommand command) {
        var order = orderService.getOrderById(command.orderId());
        var point = pointService.getPointByUserId(order.getUser());

        pointService.use(point, order.getTotalAmount());
        orderService.confirmOrder(order);

        pointService.save(point);
        orderService.saveOrder(order);
        return PayResult.of(order);
    }
}
