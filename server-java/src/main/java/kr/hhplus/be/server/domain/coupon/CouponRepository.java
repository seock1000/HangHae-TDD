package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.user.User;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface CouponRepository {
    List<UserCouponInfo> findUserCouponInfosByUserId(long userId);
    Optional<Coupon> findCouponById(Long couponId);
    Optional<UserCoupon> findUserCouponById(Long userCouponId);

    boolean existsUserCouponByUserAndCoupon(User user, Coupon coupon);

    void saveUserCoupon(UserCoupon userCoupon);
    void saveCoupon(Coupon coupon);
}
