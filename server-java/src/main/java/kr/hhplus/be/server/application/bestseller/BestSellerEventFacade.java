package kr.hhplus.be.server.application.bestseller;

import kr.hhplus.be.server.application.payment.OrderPaidEvent;
import kr.hhplus.be.server.domain.bestseller.BestSellerService;
import kr.hhplus.be.server.domain.bestseller.SalesStat;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class BestSellerEventFacade {

    private final BestSellerService bestSellerService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePaidOrder(OrderPaidEvent event) {
        event.getOrder().getOrderItems()
                .forEach(it -> bestSellerService.updateDailySalesStat(SalesStat.of(it)));
    }
}
