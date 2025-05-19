package kr.hhplus.be.server.infrastructure.coupon.worker;

import kr.hhplus.be.server.application.coupon.IssueCouponCommand;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.UserCoupon;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CouponRedisRepository {

    Optional<Coupon> findById(Long couponId);

    void save(Coupon coupon);

    void saveIssuedHistory(UserCoupon userCoupon);

    boolean existsByUserIdAndCouponId(Long userId, Long couponId);

    void saveIssueCommand(IssueCouponCommand command);

    List<Coupon> findAllCoupon();

    Map<Long, List<Long>> getTopIssueRequestPerCouponWithSize(int batchSize);

    void removeIssueRequest(List<UserCoupon> userCoupons);
}
