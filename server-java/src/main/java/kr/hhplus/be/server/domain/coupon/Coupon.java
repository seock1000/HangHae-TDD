package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.*;
import kr.hhplus.be.server.ApiError;
import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.config.jpa.BaseTimeEntity;
import kr.hhplus.be.server.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private BigDecimal discountValue;
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int stock;

    public UserCoupon issue(User user) {
        if(!this.isValid()) {
            throw ApiException.of(ApiError.COUPON_EXPIRED);
        }
        if(this.stock <= 0) {
            throw ApiException.of(ApiError.COUPON_OUT_OF_STOCK);
        }
        this.stock--;
        return UserCoupon.createWithUserAndCoupon(user, this);
    }

    public boolean isValid() {
        return this.startDate.isBefore(LocalDateTime.now()) && this.endDate.isAfter(LocalDateTime.now());
    }

    protected int discount(int amount) {
        if (this.discountType == DiscountType.AMOUNT) {
            return this.discountValue.intValue();
        } else if (this.discountType == DiscountType.RATE) {
            return (int) (amount * this.discountValue.doubleValue() / 100);
        } else {
            throw ApiException.of(ApiError.INVALID_DISCOUNT_TYPE);
        }
    }
}
