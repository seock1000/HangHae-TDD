package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.coupon.error.ExpiredCouponError;
import kr.hhplus.be.server.domain.coupon.error.OutOfStockCouponError;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

class CouponTest {

    @Test
    @DisplayName("쿠폰 발급 시 만료일이 지났으면 발급에 실패하며 ExpiredCouponError를 던진다.")
    void testCouponIssueWithExpiredDate() {
        // given
        Coupon coupon = Instancio.of(Coupon.class)
                .set(field(Coupon::getEndDate), LocalDateTime.now().minusDays(1)) // 만료일을 현재 시간보다 이전으로 설정
                .create();

        // when
        Exception exception = assertThrows(ExpiredCouponError.class, () -> {
            coupon.issueByUserId(1L);
        });

        // then
        assertEquals("쿠폰이 만료되었습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("쿠폰 발급 시 쿠폰 재고가 없으면 발급에 실패하며 OutOfStockCouponError를 던진다.")
    void testCouponIssueWithOutOfStock() {
        // given
        Coupon coupon = Instancio.of(Coupon.class)
                .set(field(Coupon::getStock), 0) // 재고를 0으로 설정
                .create();

        // when
        Exception exception = assertThrows(OutOfStockCouponError.class, () -> {
            coupon.issueByUserId(1L);
        });

        // then
        assertEquals("쿠폰 재고가 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("쿠폰 발급 시 유효한 쿠폰이면 발급에 성공한다.")
    void testCouponIssueWithValidCoupon() {
        // given
        Coupon coupon = Instancio.of(Coupon.class)
                .set(field(Coupon::getEndDate), LocalDateTime.now().plusDays(1)) // 만료일을 현재 시간보다 이후로 설정
                .set(field(Coupon::getStock), 10) // 재고를 10으로 설정
                .create();

        // when
        UserCoupon userCoupon = coupon.issueByUserId(1L);

        // then
        assertNotNull(userCoupon);
        assertEquals(1L, userCoupon.getUserId());
        assertEquals(coupon, userCoupon.getCoupon());
    }


}