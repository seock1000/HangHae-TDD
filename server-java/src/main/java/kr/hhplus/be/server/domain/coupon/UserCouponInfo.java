package kr.hhplus.be.server.domain.coupon;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class UserCouponInfo {
    private Long id;
    private Long couponId;
    private String title;
    private boolean isUsed;
    private int discountedAmount;
    private BigDecimal discountValue;
    private DiscountType discountType;
    private LocalDateTime issuedAt;
    private LocalDateTime endDate;

    private UserCouponInfo(Long id, Long couponId, String title, boolean isUsed,
                            BigDecimal discountValue, DiscountType discountType, LocalDateTime issuedAt,
                            LocalDateTime endDate) {
        this.id = id;
        this.couponId = couponId;
        this.title = title;
        this.isUsed = isUsed;
        this.discountValue = discountValue;
        this.discountType = discountType;
        this.issuedAt = issuedAt;
        this.endDate = endDate;
    }

    public static UserCouponInfo of(UserCoupon userCoupon) {
        return new UserCouponInfo(
                userCoupon.getId(),
                userCoupon.getCoupon().getId(),
                userCoupon.getCoupon().getTitle(),
                userCoupon.isUsed(),
                userCoupon.getCoupon().getDiscountValue(),
                userCoupon.getCoupon().getDiscountType(),
                userCoupon.getIssuedAt(),
                userCoupon.getCoupon().getEndDate()
        );
    }
}
