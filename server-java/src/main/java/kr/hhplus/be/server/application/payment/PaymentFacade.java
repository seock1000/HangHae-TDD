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

    /**
     * 결제 Facade
     * TC
     * 결제 흐름만 테스트 - mock
     */
    public PaymentResult pay(PayCommand command) {
        return null;
    }
}
