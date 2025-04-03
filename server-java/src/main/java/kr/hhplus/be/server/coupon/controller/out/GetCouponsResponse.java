package kr.hhplus.be.server.coupon.controller.out;

import java.time.LocalDate;

public record GetCouponsResponse(
        Long id,
        String title,
        String discountType,
        Integer discountValue,
        LocalDate startDate,
        LocalDate endDate
) {
}
