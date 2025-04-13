package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.ApiError;
import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.domain.user.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserCoupon {
    private Long id;
    private User user;
    private Coupon coupon;
    private boolean isUsed;
    private LocalDateTime issuedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private UserCoupon(User user, Coupon coupon, boolean isUsed, LocalDateTime issuedAt) {
        this.user = user;
        this.coupon = coupon;
        this.isUsed = isUsed;
        this.issuedAt = issuedAt;
    }

    public static UserCoupon createWithUserAndCoupon(User user, Coupon coupon) {
        return new UserCoupon(user, coupon, false, LocalDateTime.now());
    }

    public void use() {
        if (isUsed) {
            throw ApiException.of(ApiError.COUPON_ALREADY_USED);
        }
        if (!this.coupon.isValid()) {
            throw ApiException.of(ApiError.COUPON_EXPIRED);
        }
        this.isUsed = true;
    }

    /**
     * 테스트 불필요
     */
    public int discount(int amount) {
        return this.coupon.discount(amount);
    }

    /**
     * 테스트 불필요
     */
    public void init() {
        this.isUsed = false;
    }

}
