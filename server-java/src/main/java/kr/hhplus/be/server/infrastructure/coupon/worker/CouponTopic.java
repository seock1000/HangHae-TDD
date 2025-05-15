package kr.hhplus.be.server.infrastructure.coupon.worker;

import lombok.Getter;

@Getter
public enum CouponTopic {
    COUPON_UPDATE("COUPON:UPDATE"),
    USER_COUPON_UPDATE("USER_COUPON:UPDATE");

    final String key;

    CouponTopic(String key) {
        this.key = key;
    }
}
