package kr.hhplus.be.server.presentation.dataplatform;

import kr.hhplus.be.server.config.kafka.KafkaEventKey;
import kr.hhplus.be.server.domain.dataplatform.DataPlatformService;
import kr.hhplus.be.server.domain.order.OrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataPlatformEventListener {

    private final DataPlatformService dataPlatformService;

    private static final String DATA_PLATFORM_CONSUMER_GROUP = "data-platform-group";

    @KafkaListener(
            topics = KafkaEventKey.ORDER_CONFIRMED,
            groupId = DATA_PLATFORM_CONSUMER_GROUP,
            containerFactory = "kafkaListenerContainerFactory")
    public void handleOrderConfirmedEvent(OrderEvent.Confirmed event, Acknowledgment acknowledgment) {
        //log.info("Received order confirmed event: {}", event);
        var orderData = DataPlatformMapper.from(event);
        dataPlatformService.handleOrderData(orderData);
        acknowledgment.acknowledge();
    }
}
