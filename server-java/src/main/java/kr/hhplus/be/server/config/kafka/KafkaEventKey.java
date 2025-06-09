package kr.hhplus.be.server.config.kafka;

import lombok.Getter;

public interface KafkaEventKey {
    public static final String ORDER_CONFIRMED = "order-confirmed";
    public static final String COUPON_ISSUE = "coupon-issue";
    public static final String COUPON_ISSUED = "coupon-issued";
    public static final String COUPON_ISSUE_FAILED = "coupon-issue-failed";
}
