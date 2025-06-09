package kr.hhplus.be.server.presentation.coupon;

import kr.hhplus.be.server.application.coupon.CouponFacade;
import kr.hhplus.be.server.config.kafka.KafkaEventKey;
import kr.hhplus.be.server.domain.coupon.CouponEvent;
import kr.hhplus.be.server.domain.order.OrderEvent;
import kr.hhplus.be.server.presentation.dataplatform.DataPlatformMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponEventListener {

    private final CouponFacade couponFacade;

    private static final String COUPON_CONSUMER_GROUP = "coupon-group";

    // 고가용성을 위해 설정한 파티션 개수와 동일하게 concurrency 설정
    @KafkaListener(
            topics = KafkaEventKey.COUPON_ISSUE,
            groupId = COUPON_CONSUMER_GROUP,
            concurrency = "5")
    public void handleCouponIssueEvent(CouponEvent.Issue event, Acknowledgment acknowledgment) {
        //log.info("Received coupon issue event: {}", event);
        couponFacade.issueCoupon(CouponMapper.from(event));
        acknowledgment.acknowledge();
    }
}
