package kr.hhplus.be.server.domain.coupon;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public record UserCouponInfo(
        Long id,
        Long couponId,
        String title,
        boolean isUsed,
        BigDecimal discountValue,
        DiscountType discountType,
        LocalDateTime issuedAt,
        LocalDateTime endDate
) {
    public UserCouponInfo of(UserCoupon userCoupon) {
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
