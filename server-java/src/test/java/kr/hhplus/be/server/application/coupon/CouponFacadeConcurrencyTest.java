package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.infrastructure.coupon.CouponJpaRepository;
import kr.hhplus.be.server.infrastructure.coupon.UserCouponJpaRepository;
import kr.hhplus.be.server.infrastructure.user.UserJpaRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CouponFacadeConcurrencyTest {

    @Autowired
    private CouponFacade couponFacade;
    @Autowired
    private CouponJpaRepository couponJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;

    @BeforeEach
    void tearDown() {
        couponJpaRepository.deleteAll();
    }

    @Test
    @DisplayName("여러 스레드에서 동시에 쿠폰 발급 요청 시 재고관리가 정확하게 되며, 발급되어야 한다.")
    void testConcurrentIssue() {
        // Given
        var coupon = couponJpaRepository.saveAndFlush(Instancio.of(Coupon.class)
                .set(field("id"), null)
                .set(field("stock"), 100)
                .set(field("startDate"), LocalDateTime.now().minusDays(1))
                .set(field("endDate"), LocalDateTime.now().plusDays(1))
                .create());

        var user1 = userJpaRepository.saveAndFlush(Instancio.of(User.class)
                .set(field("id"), null)
                .create());
        var user2 = userJpaRepository.saveAndFlush(Instancio.of(User.class)
                .set(field("id"), null)
                .create());
        var user3 = userJpaRepository.saveAndFlush(Instancio.of(User.class)
                .set(field("id"), null)
                .create());
        var user4 = userJpaRepository.saveAndFlush(Instancio.of(User.class)
                .set(field("id"), null)
                .create());

        // When
        CompletableFuture<Void> issue1 = CompletableFuture.runAsync(() -> {
            try {
                couponFacade.issueCoupon(new IssueCouponCommand(user1.getId(), coupon.getId()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        CompletableFuture<Void> issue2 = CompletableFuture.runAsync(() -> {
            try {
                couponFacade.issueCoupon(new IssueCouponCommand(user2.getId(), coupon.getId()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        CompletableFuture<Void> issue3 = CompletableFuture.runAsync(() -> {
            try {
                couponFacade.issueCoupon(new IssueCouponCommand(user3.getId(), coupon.getId()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        CompletableFuture<Void> issue4 = CompletableFuture.runAsync(() -> {
            try {
                couponFacade.issueCoupon(new IssueCouponCommand(user4.getId(), coupon.getId()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        CompletableFuture.allOf(issue1, issue2, issue3, issue4).join();

        // Then
        var updatedCoupon = couponJpaRepository.findById(coupon.getId()).orElseThrow();
        assertEquals(96, updatedCoupon.getStock());
    }

}