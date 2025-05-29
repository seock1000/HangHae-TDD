package kr.hhplus.be.server.infrastructure.coupon.event;

import kr.hhplus.be.server.config.kafka.KafkaEventKey;
import kr.hhplus.be.server.domain.coupon.CouponEvent;
import kr.hhplus.be.server.domain.coupon.CouponEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaCouponEventPublisher implements CouponEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publishIssueEvent(CouponEvent.Issue event) {
        // 쿠폰 ID를 메시지 키로 사용하여 순차적 처리 보장
        kafkaTemplate.send(KafkaEventKey.COUPON_ISSUE, event.couponId().toString(), event);
    }
}
