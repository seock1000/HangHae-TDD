package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.IntegrationTestSupport;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.GetUserCouponCommand;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.infrastructure.coupon.persistence.CouponJpaRepository;
import kr.hhplus.be.server.infrastructure.coupon.persistence.UserCouponJpaRepository;
import kr.hhplus.be.server.infrastructure.user.UserJpaRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

class CouponFacadeIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private CouponFacade couponFacade;

    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private UserCouponJpaRepository userCouponJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;

    @Test
    @DisplayName("쿠폰 조회 시, 받은 유저 ID에 대한 쿠폰이 없으면 빈 리스트를 반환한다.")
    void getUserCouponInfosByUserWithEmpty() {
        // given
        var command = new GetUserCouponCommand(1L);

        // when
        var result = couponFacade.getUserCouponInfosByUser(command);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("쿠폰 조회 시, 받은 유저 ID에 대한 쿠폰이 있으면 쿠폰 리스트를 반환한다.")
    void getUserCouponInfosByUser() throws InterruptedException {
        // given
        var command = new GetUserCouponCommand(1L);
        var coupon = Instancio.of(Coupon.class)
                .set(field("id"), null)
                .create();
        var userCoupon = Instancio.of(UserCoupon.class)
                .set(field("id"), null)
                .set(field("coupon"), coupon)
                .set(field("userId"), command.userId())
                .create();
        couponRepository.saveCoupon(coupon);
        couponRepository.saveUserCoupon(userCoupon);

        Thread.sleep(100);

        // when
        var result = couponFacade.getUserCouponInfosByUser(command);

        // then
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("유효한 쿠폰 발급 요청 시, 쿠폰이 발급된다.")
    void issueCoupon() {
        // given
        var coupon = Instancio.of(Coupon.class)
                .set(field("id"), null)
                .set(field("startDate"), LocalDateTime.now().minusDays(1))
                .set(field("endDate"), LocalDateTime.now().plusDays(1))
                .create();
        var user = Instancio.of(User.class)
                .set(field("id"), null)
                .create();
        userJpaRepository.saveAndFlush(user);
        couponRepository.saveCoupon(coupon);

        var command = new IssueCouponCommand(user.getId(), coupon.getId());

        // when
        var result = couponFacade.issueCoupon(command);

        // then
        assertNotNull(result);
    }
}