package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.ApiError;
import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.domain.user.User;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

class UserCouponTest {

    @Test
    @DisplayName("쿠폰 생성 시, User와 Coupon이 주어지면 미사용 상태로 UserCoupon이 생성된다")
    void createUserCoupon() {
        // given
        User user = Instancio.create(User.class);
        Coupon coupon = Instancio.create(Coupon.class);

        // when
        UserCoupon userCoupon = UserCoupon.createWithUserAndCoupon(user, coupon);

        // then
        assertNotNull(userCoupon);
        assertEquals(user.getId(), userCoupon.getUserId());
        assertEquals(coupon, userCoupon.getCoupon());
        assertFalse(userCoupon.isUsed());
    }

    @Test
    @DisplayName("쿠폰 사용 시, 이미 사용된 쿠폰이면 ApiException(COUPON_ALREADY_USED)이 발생한다")
    void useUserCouponWhenAlreadyUsed() {
        // given
        UserCoupon userCoupon = Instancio.of(UserCoupon.class)
                .set(field("isUsed"), true)
                .create();

        // when, then
        ApiException exception = assertThrows(ApiException.class, userCoupon::use);
        assertEquals(ApiError.COUPON_ALREADY_USED, exception.getApiError());
    }

    @Test
    @DisplayName("쿠폰 사용 시, 쿠폰이 만료되었으면 ApiException(COUPON_EXPIRED)이 발생한다")
    void useUserCouponWhenCouponExpired() {
        // given
        Coupon coupon = Instancio.of(Coupon.class)
                .set(field("startDate"), LocalDateTime.now().minusDays(2))
                .set(field("endDate"), LocalDateTime.now().minusDays(1))
                .create();
        UserCoupon userCoupon = Instancio.of(UserCoupon.class)
                .set(field("coupon"), coupon)
                .set(field("isUsed"), false)
                .create();

        // when, then
        ApiException exception = assertThrows(ApiException.class, userCoupon::use);
        assertEquals(ApiError.COUPON_EXPIRED, exception.getApiError());
    }

    @Test
    @DisplayName("쿠폰 사용 시, 쿠폰이 유효하면 사용 상태로 변경된다")
    void useUserCouponWhenCouponValid() {
        // given
        Coupon coupon = Instancio.of(Coupon.class)
                .set(field("startDate"), LocalDateTime.now().minusDays(1))
                .set(field("endDate"), LocalDateTime.now().plusDays(1))
                .create();
        UserCoupon userCoupon = Instancio.of(UserCoupon.class)
                .set(field("coupon"), coupon)
                .set(field("isUsed"), false)
                .create();

        // when
        userCoupon.use();

        // then
        assertTrue(userCoupon.isUsed());
    }

}