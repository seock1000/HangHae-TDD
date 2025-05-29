package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.user.User;

public class CouponEvent {

    public record Issue(
            Long userId,
            Long couponId
    ) {
        public Issue {
            if (userId == null || userId <= 0) {
                throw new IllegalArgumentException("잘못된 유저입니다.");
            }
            if (couponId == null || couponId <= 0) {
                throw new IllegalArgumentException("잘못된 쿠폰입니다.");
            }
        }
    }

    public record Issued(
            Long userId,
            Long userCouponId,
            String couponName
    ) {
        public static Issued of(User user, Coupon coupon, UserCoupon userCoupon) {
            return new Issued(user.getId(), userCoupon.getId(), coupon.getTitle());
        }
    }

    public record IssueFailed(
            Long userId,
            Long couponId,
            Exception ex
    ) {
        public static IssueFailed of(Long userId, Long couponId, Exception ex) {
            return new IssueFailed(userId, couponId, ex);
        }
    }
}
