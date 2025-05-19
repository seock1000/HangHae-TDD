package kr.hhplus.be.server.infrastructure.coupon.worker;

import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.UserCouponInfo;

public class CouponMapper {

    public static UserCouponInfo UserCouponToUserCouponInfo(UserCoupon userCoupon) {
        return new UserCouponInfo(
                userCoupon.getId(),
                userCoupon.getCoupon().getId(),
                userCoupon.getCoupon().getTitle(),
                userCoupon.isUsed(),
                userCoupon.getCoupon().getDiscountValue(),
                userCoupon.getCoupon().getDiscountType(),
                userCoupon.getIssuedAt(),
                userCoupon.getCoupon().getEndDate()
        );
    }
}
