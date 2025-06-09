package kr.hhplus.be.server.presentation.bestseller;

import kr.hhplus.be.server.application.bestseller.BestSellerFacade;
import kr.hhplus.be.server.application.bestseller.UpdateBestSellerCommand;
import kr.hhplus.be.server.config.kafka.KafkaEventKey;
import kr.hhplus.be.server.domain.bestseller.BestSellerService;
import kr.hhplus.be.server.domain.order.OrderEvent;
import kr.hhplus.be.server.domain.payment.PaymentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class BestSellerEventListener {

    private final BestSellerFacade bestSellerFacade;

    private static final String BEST_SELLER_CONSUMER_GROUP = "best-seller-group";

    @KafkaListener(
            topics = KafkaEventKey.ORDER_CONFIRMED,
            groupId = BEST_SELLER_CONSUMER_GROUP,
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handlePayment(OrderEvent.Confirmed event, Acknowledgment acknowledgment) {
        log.info("Received order confirmed event: {}", event);
        bestSellerFacade.updateDailyBastSeller(BestSellerMapper.from(event));
        acknowledgment.acknowledge();
    }
}
