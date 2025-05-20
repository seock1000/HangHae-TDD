package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.event.PaidOrderEvent;
import kr.hhplus.be.server.domain.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class OrderEventListener {

    private final OrderService orderService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePaidOrder(PaidOrderEvent event) {
        event.toOrderData()
                .forEach(orderService::sendOrderData);
    }
}
