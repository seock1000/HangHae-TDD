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

}
