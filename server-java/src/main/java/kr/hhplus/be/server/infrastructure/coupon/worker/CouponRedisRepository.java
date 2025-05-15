package kr.hhplus.be.server.infrastructure.coupon.worker;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.UserCoupon;

import java.util.List;
import java.util.Optional;

public interface CouponRedisRepository {

    Optional<Coupon> findById(Long couponId);

    void save(Coupon coupon);

    void saveIssuedHistory(UserCoupon userCoupon);

    boolean existsByUserIdAndCouponId(Long userId, Long couponId);
}
