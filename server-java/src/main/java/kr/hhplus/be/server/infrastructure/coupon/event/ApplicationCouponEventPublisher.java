package kr.hhplus.be.server.infrastructure.coupon.event;

import kr.hhplus.be.server.domain.coupon.CouponEvent;
import kr.hhplus.be.server.domain.coupon.CouponEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationCouponEventPublisher implements CouponEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publishIssueEvent(CouponEvent.Issue event) {
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishIssuedEvent(CouponEvent.Issued event) {
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishIssueFailEvent(CouponEvent.IssueFailed event) {
        applicationEventPublisher.publishEvent(event);
    }
}
