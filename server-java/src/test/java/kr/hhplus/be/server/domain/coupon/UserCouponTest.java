package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.coupon.error.AlreadyUsedCouponError;
import kr.hhplus.be.server.domain.coupon.error.ExpiredCouponError;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.C;

import java.time.LocalDateTime;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

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
    @DisplayName("쿠폰 사용 시, 유효한 쿠폰이면 사용에 성공하고 사용 상태로 변경된다.")
    void useValidCoupon() {
        // given
        Coupon coupon = Instancio.of(Coupon.class)
                .set(field(Coupon::getEndDate), LocalDateTime.now().plusDays(1))
                .create();
        UserCoupon userCoupon = UserCoupon.createWithUserIdAndCoupon(1L, coupon);

        // when
        userCoupon.use();

        // then
        assertTrue(userCoupon.isUsed());
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
        Exception exception = assertThrows(AlreadyUsedCouponError.class, () -> userCoupon.use());

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
        Exception exception = assertThrows(ExpiredCouponError.class, () -> userCoupon.use());

        // then
        assertEquals("쿠폰이 만료되었습니다.", exception.getMessage());
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