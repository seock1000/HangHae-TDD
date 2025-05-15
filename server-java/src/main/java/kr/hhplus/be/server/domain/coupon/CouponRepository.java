package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.application.coupon.IssueCouponCommand;
import kr.hhplus.be.server.domain.user.User;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CouponRepository {
    List<UserCouponInfo> findUserCouponInfosByUserId(long userId);
    Optional<Coupon> findCouponById(Long couponId);
    public Optional<Coupon> findCouponByIdForUpdate(Long couponId);
    Optional<UserCoupon> findUserCouponById(Long userCouponId);

    boolean existsUserCouponByUserAndCoupon(User user, Coupon coupon);

    UserCoupon saveUserCoupon(UserCoupon userCoupon);
    Coupon saveCoupon(Coupon coupon);

    void saveIssueCommand(IssueCouponCommand command);

    List<Coupon> findAllCoupon();

    Map<Long, List<Long>> getTopIssueRequestPerCouponWithSize(int batchSize);

    void removeIssueRequest(List<UserCoupon> userCoupons);
}
