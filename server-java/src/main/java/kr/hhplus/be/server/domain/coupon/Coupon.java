package kr.hhplus.be.server.domain.coupon;

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
        return null;
    }
}
