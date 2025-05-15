package kr.hhplus.be.server.infrastructure.coupon.worker;

import kr.hhplus.be.server.IntegrationTestSupport;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.infrastructure.coupon.persistence.CouponJpaRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

class CouponSyncWorkerTest extends IntegrationTestSupport {

    @Autowired
    private CouponSyncWorker couponSyncWorker;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CouponJpaRepository couponJpaRepository;

    @Test
    @DisplayName("Redis에 저장된 쿠폰 정보를 DB에 동기화한다.")
    void redis에_저장된_쿠폰_정보로_동기화되어야_한다() {
        // given
        LocalDateTime couponStartDate = LocalDateTime.now().minusDays(1);
        LocalDateTime couponEndDate = LocalDateTime.now().plusDays(1);
        Coupon coupon = Instancio.of(Coupon.class)
                .set(field("id"), null)
                .set(field("stock"), 100)
                .set(field("startDate"), couponStartDate)
                .set(field("endDate"), couponEndDate)
                .create();
        coupon = couponRepository.saveCoupon(coupon);
        User user = Instancio.of(User.class)
                .set(field("id"), null)
                .create();
        user = userRepository.save(user);
        coupon.issue(user);
        couponRepository.saveCoupon(coupon);

        Coupon dbCoupon = couponJpaRepository.findById(coupon.getId()).orElseThrow();

        // when
        couponSyncWorker.syncUpdateCoupon();

        // then
        Coupon updatedCoupon = couponJpaRepository.findById(coupon.getId()).orElseThrow();
        assertNotEquals(coupon.getStock(), dbCoupon.getStock());
        assertEquals(coupon.getStock(), updatedCoupon.getStock());
    }
}