package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.coupon.error.ExpiredCouponError;
import kr.hhplus.be.server.domain.coupon.error.OutOfStockCouponError;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class Coupon {
    private Long id;
    private String title;
    private BigDecimal discountValue;
    private DiscountType discountType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int stock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 쿠폰을 발급
     * TC
     * 만료일이 지났으면 발급에 실패한다. => ExpiredCouponError
     * 쿠폰 재고가 없으면 발급에 실패한다. => OutOfStockCouponError
     */
    public UserCoupon issueByUserId(Long userId) {
        if (endDate.isBefore(LocalDateTime.now())) {
            throw ExpiredCouponError.of("쿠폰이 만료되었습니다.");
        }
        if (stock <= 0) {
            throw OutOfStockCouponError.of("쿠폰 재고가 없습니다.");
        }
        this.stock--;
        return UserCoupon.createWithUserIdAndCoupon(userId, this);
    }
}
