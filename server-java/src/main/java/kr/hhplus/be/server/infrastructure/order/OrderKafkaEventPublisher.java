package kr.hhplus.be.server.infrastructure.order;

import kr.hhplus.be.server.application.order.OrderEventPublisher;
import kr.hhplus.be.server.config.kafka.KafkaEventKey;
import kr.hhplus.be.server.domain.order.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderKafkaEventPublisher implements OrderEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publishConfirmEvent(OrderEvent.Confirmed event) {
        kafkaTemplate.send(KafkaEventKey.ORDER_CONFIRMED, event);
    }
}
