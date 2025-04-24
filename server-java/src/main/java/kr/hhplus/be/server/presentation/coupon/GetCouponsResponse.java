package kr.hhplus.be.server.presentation.coupon;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.domain.coupon.DiscountType;
import kr.hhplus.be.server.domain.coupon.GetUserCouponCommand;
import kr.hhplus.be.server.domain.coupon.UserCouponInfo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record GetCouponsResponse(
        @Schema(description = "쿠폰 ID")
        Long id,
        @Schema(description = "쿠폰 이름")
        String title,
        @Schema(description = "쿠폰 할인 타입")
        DiscountType discountType,
        @Schema(description = "쿠폰 할인 값")
        BigDecimal discountValue,
        @Schema(description = "유효기간 시작일")
        LocalDateTime startAt,
        @Schema(description = "유효기간 종료일")
        LocalDateTime endAt
) {
        public static GetCouponsResponse of(
                UserCouponInfo userCouponInfo
        ) {
                return new GetCouponsResponse(
                        userCouponInfo.id(),
                        userCouponInfo.title(),
                        userCouponInfo.discountType(),
                        userCouponInfo.discountValue(),
                        userCouponInfo.issuedAt(),
                        userCouponInfo.endDate()
                );
        }
}
