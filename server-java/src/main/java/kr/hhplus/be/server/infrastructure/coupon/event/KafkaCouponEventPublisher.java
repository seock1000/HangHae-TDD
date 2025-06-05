package kr.hhplus.be.server.infrastructure.coupon.event;

import kr.hhplus.be.server.config.kafka.KafkaEventKey;
import kr.hhplus.be.server.domain.coupon.CouponEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class KafkaCouponEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    // 트랜잭션 커밋 후 이벤트 발행 보장을 위해 분리
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishIssueEvent(CouponEvent.Issue event) {
        // 쿠폰 ID를 메시지 키로 사용하여 같은 파티션에 적재되도록 하여 같은 쿠폰에 대한 발급 요청은 순차적 처리 보장
        kafkaTemplate.send(KafkaEventKey.COUPON_ISSUE, event.couponId().toString(), event);
    }

    // 완전히 성공(커밋)한 후 이벤트 발행
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishIssuedEvent(CouponEvent.Issued event) {
        // 해당 메시지는 알림 서비스에서 consume 하므로 순서는 중요하지 않으므로 키 미설정
        kafkaTemplate.send(KafkaEventKey.COUPON_ISSUED, event);
    }

    // 완전히 실패(롤백)한 후 이벤트 발행
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void publishIssueFailedEvent(CouponEvent.IssueFailed event) {
        // 해당 메시지는 알림 서비스에서 consume 하므로 순서는 중요하지 않으므로 키 미설정
        kafkaTemplate.send(KafkaEventKey.COUPON_ISSUE_FAILED, event);
    }
}
