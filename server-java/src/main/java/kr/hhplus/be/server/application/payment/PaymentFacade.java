package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.order.Orders;
import kr.hhplus.be.server.domain.point.PointService;
import kr.hhplus.be.server.domain.point.command.UsePointCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PaymentFacade {
    private final OrderService orderService;
    private final PointService pointService;
    private final DataPlatformSender dataPlatformSender;

    /**
     * 결제 Facade
     * TC
     * 결제 흐름만 테스트 - mock
     */
    public PaymentResult pay(PayCommand command) {
        // 주문 확정
        Orders order = orderService.confirmOrder(command.orderId());
        // 포인트 사용
        UsePointCommand pointCmd = new UsePointCommand(order.getUser(), order.getTotalAmount());
        pointService.use(pointCmd);

        try {
            // 주문 정보 데이터 플랫폼 전송
            dataPlatformSender.sendOrder(order);
        } catch (Exception e) {
            log.error("DataPlatformSender sendOrder error", e);
        }
        return PaymentResult.of(order);
    }
}
