package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.coupon.error.AlreadyUsedCouponError;
import kr.hhplus.be.server.domain.coupon.error.ExpiredCouponError;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.C;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class UserCouponTest {

    @Test
    @DisplayName("UserCoupon 생성 시, 사용자 ID와 Coupon 객체를 전달하여 미사용 상태로 생성한다.")
    void createWithUserIdAndCoupon() {
        // given
        Long userId = 1L;
        Coupon coupon = Instancio.create(Coupon.class);

        // when
        UserCoupon userCoupon = UserCoupon.createWithUserIdAndCoupon(userId, coupon);

        // then
        assertEquals(userId, userCoupon.getUserId());
        assertEquals(coupon, userCoupon.getCoupon());
        assertFalse(userCoupon.isUsed());
    }

    @Test
    @DisplayName("쿠폰 사용 시, 이미 사용된 쿠폰이면 사용에 실패한다.")
    void useAlreadyUsedCoupon() {
        // given
        Coupon coupon = Instancio.of(Coupon.class)
                .set(field(Coupon::getEndDate), LocalDateTime.now().plusDays(1))
                .create();
        UserCoupon userCoupon = Instancio.of(UserCoupon.class)
                .set(field(UserCoupon::getCoupon), coupon)
                .set(field(UserCoupon::isUsed), true)
                .create();

        // when
        Exception exception = assertThrows(AlreadyUsedCouponError.class, () -> userCoupon.use(anyInt()));

        // then
        assertEquals("이미 사용된 쿠폰입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("쿠폰 사용 시, 만료일이 지났으면 사용에 실패한다.")
    void useExpiredCoupon() {
        // given
        Coupon coupon = Instancio.of(Coupon.class)
                .set(field(Coupon::getEndDate), LocalDateTime.now().minusDays(1))
                .create();
        UserCoupon userCoupon = UserCoupon.createWithUserIdAndCoupon(1L, coupon);

        // when
        Exception exception = assertThrows(ExpiredCouponError.class, () -> userCoupon.use(anyInt()));

        // then
        assertEquals("쿠폰이 만료되었습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("쿠폰 사용 시, 유효한 쿠폰이며 정액제인 경우 그 액수를 할인 금액으로 저장하고 사용 상태로 변경한다.")
    void useCouponWithDiscount() {
        // given
        Coupon coupon = Instancio.of(Coupon.class)
                .set(field(Coupon::getDiscountType), DiscountType.AMOUNT)
                .set(field(Coupon::getDiscountValue), BigDecimal.valueOf(1000))
                .set(field(Coupon::getEndDate), LocalDateTime.now().plusDays(1))
                .create();
        UserCoupon userCoupon = UserCoupon.createWithUserIdAndCoupon(1L, coupon);

        // when
        userCoupon.use(5000);

        // then
        assertEquals(1000, userCoupon.getDiscountedAmount());
        assertTrue(userCoupon.isUsed());
    }

    @Test
    @DisplayName("쿠폰 사용 시, 유효한 쿠폰이며 정률제인 경우 그 비율에 따라 할인 금액을 계산하고 사용 상태로 변경한다.")
    void useCouponWithRateDiscount() {
        // given
        Coupon coupon = Instancio.of(Coupon.class)
                .set(field(Coupon::getDiscountType), DiscountType.RATE)
                .set(field(Coupon::getDiscountValue), BigDecimal.valueOf(20))
                .set(field(Coupon::getEndDate), LocalDateTime.now().plusDays(1))
                .create();
        UserCoupon userCoupon = UserCoupon.createWithUserIdAndCoupon(1L, coupon);

        // when
        userCoupon.use(5000);

        // then
        assertEquals(1000, userCoupon.getDiscountedAmount());
        assertTrue(userCoupon.isUsed());
    }


    @Test
    @DisplayName("쿠폰 초기화 시, 사용 상태가 false로 초기화된다.")
    void initCoupon() {
        // given
        UserCoupon userCoupon = Instancio.of(UserCoupon.class)
                .set(field(UserCoupon::isUsed), true)
                .create();

        // when
        userCoupon.init();

        // then
        assertFalse(userCoupon.isUsed());
    }

}