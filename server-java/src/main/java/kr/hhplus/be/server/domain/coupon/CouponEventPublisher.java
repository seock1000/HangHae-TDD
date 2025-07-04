package kr.hhplus.be.server.domain.coupon;


public interface CouponEventPublisher {

    void publishIssueEvent(CouponEvent.Issue event);

    void publishIssuedEvent(CouponEvent.Issued event);

    void publishIssueFailEvent(CouponEvent.IssueFailed event);
}
