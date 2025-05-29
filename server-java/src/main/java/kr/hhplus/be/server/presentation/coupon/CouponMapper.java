package kr.hhplus.be.server.presentation.coupon;

import kr.hhplus.be.server.application.coupon.IssueCouponCommand;
import kr.hhplus.be.server.domain.coupon.CouponEvent;

public class CouponMapper {

    public static IssueCouponCommand from(CouponEvent.Issue event) {
        return new IssueCouponCommand(event.userId(), event.couponId());
    }
}
