package kr.hhplus.be.server.domain.coupon;

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
}
