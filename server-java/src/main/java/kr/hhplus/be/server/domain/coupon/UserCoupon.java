package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.coupon.error.AlreadyUsedCouponError;
import kr.hhplus.be.server.domain.coupon.error.ExpiredCouponError;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserCoupon {
    private Long id;
    private Long userId;
    private Coupon coupon;
    private boolean isUsed;
    private LocalDateTime issuedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private UserCoupon(Long userId, Coupon coupon, boolean isUsed, LocalDateTime issuedAt) {
        this.userId = userId;
        this.coupon = coupon;
        this.isUsed = isUsed;
        this.issuedAt = issuedAt;
    }

    public static UserCoupon createWithUserIdAndCoupon(Long userId, Coupon coupon) {
        return new UserCoupon(userId, coupon, false, LocalDateTime.now());
    }

    /**
     * 쿠폰 사용
     * TC
     * 이미 사용된 쿠폰이면 사용에 실패한다. => AlreadyUsedCouponError
     * 만료일이 지났으면 사용에 실패한다. => ExpiredCouponError
     */
    public void use() {
        if (isUsed) {
            throw AlreadyUsedCouponError.of("이미 사용된 쿠폰입니다.");
        }
        if (coupon.getEndDate().isBefore(LocalDateTime.now())) {
            throw ExpiredCouponError.of("쿠폰이 만료되었습니다.");
        }
        this.isUsed = true;
    }

    /**
     * 쿠폰의 사용 상태를 초기화 한다.
     * TC
     * 성공 케이스 밖에 없음.
     */
    public void init() {
        this.isUsed = false;
    }
}
