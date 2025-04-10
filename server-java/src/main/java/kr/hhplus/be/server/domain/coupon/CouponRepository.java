package kr.hhplus.be.server.domain.coupon;

import java.util.Optional;

public interface CouponRepository {
    Optional<Coupon> findCouponById(long couponId);

    boolean existsUserCouponByUserIdAndCoupon(long userId, Coupon coupon);

    UserCoupon saveUserCoupon(UserCoupon userCoupon);
}
