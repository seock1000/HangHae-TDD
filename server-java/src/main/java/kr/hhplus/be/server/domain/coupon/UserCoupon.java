package kr.hhplus.be.server.domain.coupon;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserCoupon {
    private Long id;
    private Long userId;
    private Coupon coupon;
    private boolean isUsed;
    private int discountedAmount;
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

}
