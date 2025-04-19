package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.ApiError;
import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.domain.user.User;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

class CouponTest {

    @Test
    @DisplayName("쿠폰 발급 시, 현재가 쿠폰의 유효기간에 해당하고 쿠폰의 수량이 0보다 크면 UserCoupon이 발급된다")
    void createUserCoupon() {
        // given
        Coupon coupon = Instancio.of(Coupon.class)
                .set(field("startDate"), LocalDateTime.now().minusDays(1))
                .set(field("endDate"), LocalDateTime.now().plusDays(1))
                .set(field("stock"), 1)
                .create();
        User user = Instancio.create(User.class);

        // when
        UserCoupon userCoupon = coupon.issue(user);

        // then
        assertNotNull(userCoupon);
        assertEquals(user.getId(), userCoupon.getUserId());
        assertEquals(coupon, userCoupon.getCoupon());
        assertFalse(userCoupon.isUsed());
    }

    @Test
    @DisplayName("쿠폰 발급 시, 쿠폰의 유효기간이 만료되었으면 ApiException(COUPON_EXPIRED)이 발생한다")
    void createUserCouponWhenCouponExpired() {
        // given
        Coupon coupon = Instancio.of(Coupon.class)
                .set(field("startDate"), LocalDateTime.now().minusDays(2))
                .set(field("endDate"), LocalDateTime.now().minusDays(1))
                .set(field("stock"), 1)
                .create();
        User user = Instancio.create(User.class);

        // when, then
        ApiException exception = assertThrows(ApiException.class, () -> coupon.issue(user));
        assertEquals(ApiError.COUPON_EXPIRED, exception.getApiError());
    }

    @Test
    @DisplayName("쿠폰 발급 시, 쿠폰의 수량이 0보다 작거나 같으면 ApiException(COUPON_OUT_OF_STOCK)이 발생한다")
    void createUserCouponWhenCouponOutOfStock() {
        // given
        Coupon coupon = Instancio.of(Coupon.class)
                .set(field("startDate"), LocalDateTime.now().minusDays(1))
                .set(field("endDate"), LocalDateTime.now().plusDays(1))
                .set(field("stock"), 0)
                .create();
        User user = Instancio.create(User.class);

        // when, then
        ApiException exception = assertThrows(ApiException.class, () -> coupon.issue(user));
        assertEquals(ApiError.COUPON_OUT_OF_STOCK, exception.getApiError());
    }

    @Test
    @DisplayName("쿠폰 유효성 검사 시, 쿠폰의 유효기간이 아니면 false를 반환한다")
    void isValidWhenCouponExpired() {
        // given
        Coupon coupon = Instancio.of(Coupon.class)
                .set(field("startDate"), LocalDateTime.now().minusDays(2))
                .set(field("endDate"), LocalDateTime.now().minusDays(1))
                .create();

        // when
        boolean isValid = coupon.isValid();

        // then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("쿠폰 유효성 검사 시, 쿠폰의 유효기간이면 true를 반환한다")
    void isValidWhenCouponValid() {
        // given
        Coupon coupon = Instancio.of(Coupon.class)
                .set(field("startDate"), LocalDateTime.now().minusDays(1))
                .set(field("endDate"), LocalDateTime.now().plusDays(1))
                .create();

        // when
        boolean isValid = coupon.isValid();

        // then
        assertTrue(isValid);
    }

    @Test
    @DisplayName("쿠폰 할인 금액 계산 시, 할인 타입이 AMOUNT이면 정해진 할인 금액을 반환한다")
    void discountWhenDiscountTypeIsAmount() {
        // given
        Coupon coupon = Instancio.of(Coupon.class)
                .set(field("discountType"), DiscountType.AMOUNT)
                .set(field("discountValue"), BigDecimal.valueOf(1000))
                .create();

        // when
        int discountAmount = coupon.discount(5000);

        // then
        assertEquals(1000, discountAmount);
    }

    @Test
    @DisplayName("쿠폰 할인 금액 계산 시, 할인 타입이 RATE이면 정해진 비율에 따라 할인 금액을 반환한다")
    void discountWhenDiscountTypeIsRate() {
        // given
        Coupon coupon = Instancio.of(Coupon.class)
                .set(field("discountType"), DiscountType.RATE)
                .set(field("discountValue"), BigDecimal.valueOf(20))
                .create();

        // when
        int discountAmount = coupon.discount(5000);

        // then
        assertEquals(1000, discountAmount);
    }

    @Test
    @DisplayName("쿠폰 할인 금액 계산 시, 할인 타입이 잘못된 경우 ApiException(INVALID_DISCOUNT_TYPE)을 발생시킨다")
    void discountWhenInvalidDiscountType() {
        // given
        Coupon coupon = Instancio.of(Coupon.class)
                .set(field("discountType"), null)
                .set(field("discountValue"), BigDecimal.valueOf(20))
                .create();

        // when, then
        ApiException exception = assertThrows(ApiException.class, () -> coupon.discount(5000));
        assertEquals(ApiError.INVALID_DISCOUNT_TYPE, exception.getApiError());
    }

}