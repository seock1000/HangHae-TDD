package kr.hhplus.be.server.coupon.controller.out;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record GetCouponsResponse(
        @Schema(description = "쿠폰 ID")
        Long id,
        @Schema(description = "쿠폰 이름")
        String title,
        @Schema(description = "쿠폰 할인 타입")
        String discountType,
        @Schema(description = "쿠폰 할인 값")
        Integer discountValue,
        @Schema(description = "유효기간 시작일")
        LocalDate startDate,
        @Schema(description = "유효기간 종료일")
        LocalDate endDate
) {
}
