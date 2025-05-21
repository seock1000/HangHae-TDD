package kr.hhplus.be.server.presentation.order;

import kr.hhplus.be.server.application.order.ConfirmOrderCommand;
import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.domain.payment.PaymentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OrderEventListener {

    private final OrderFacade orderFacade;

    @Retryable(
            backoff = @Backoff(delay = 1000)
    )
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePayment(PaymentEvent event) {
        var command = ConfirmOrderCommand.of(event.getOrderId());
        orderFacade.confirmOrder(command);
    }
}
