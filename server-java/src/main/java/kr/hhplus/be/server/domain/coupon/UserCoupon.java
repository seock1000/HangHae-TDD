package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.*;
import kr.hhplus.be.server.ApiError;
import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.config.jpa.BaseTimeEntity;
import kr.hhplus.be.server.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCoupon extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;
    private boolean isUsed;
    private LocalDateTime issuedAt;

    private UserCoupon(Long userId, Coupon coupon, boolean isUsed, LocalDateTime issuedAt) {
        this.userId = userId;
        this.coupon = coupon;
        this.isUsed = isUsed;
        this.issuedAt = issuedAt;
    }

    public static UserCoupon createWithUserAndCoupon(User user, Coupon coupon) {
        return new UserCoupon(user.getId(), coupon, false, LocalDateTime.now());
    }

    protected void use() {
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
    public void init() {
        this.isUsed = false;
    }

    public AppliedCoupon toAppliedCoupon() {
        return AppliedCoupon.of(this);
    }

}
