package kr.hhplus.be.server.presentation.bestseller;

import kr.hhplus.be.server.application.bestseller.BestSellerFacade;
import kr.hhplus.be.server.application.bestseller.UpdateBestSellerCommand;
import kr.hhplus.be.server.domain.bestseller.BestSellerService;
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
public class BestSellerEventListener {

    private final BestSellerFacade bestSellerFacade;

    @Retryable(
            backoff = @Backoff(delay = 1000)
    )
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePayment(PaymentEvent event) {
        bestSellerFacade.updateDailyBastSeller(UpdateBestSellerCommand.of(event.getOrderId()));
    }
}
