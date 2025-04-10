package kr.hhplus.be.server.domain.coupon;

import java.util.List;
import java.util.Optional;

public interface CouponRepository {
    Optional<Coupon> findCouponById(long couponId);
    Optional<UserCoupon> findUserCouponById(long userCouponId);

    boolean existsUserCouponByUserIdAndCoupon(long userId, Coupon coupon);

    UserCoupon saveUserCoupon(UserCoupon userCoupon);

    List<UserCoupon> findUserCouponsByUserId(long userId);
}
