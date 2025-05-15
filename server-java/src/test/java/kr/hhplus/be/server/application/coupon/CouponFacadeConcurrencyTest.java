package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.IntegrationTestSupport;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

class CouponFacadeConcurrencyTest extends IntegrationTestSupport {

    @Autowired
    private CouponFacade couponFacade;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("여러 스레드에서 동시에 쿠폰 발급 요청 시 재고관리가 정확하게 되며, 발급되어야 한다.")
    void 여러_스레드에서_동시에_쿠폰_발급시_재고관리가_정확하게_되며_발급되어야_한다() {
        // Given
        var coupon = couponRepository.saveCoupon(Instancio.of(Coupon.class)
                .set(field("id"), null)
                .set(field("stock"), 100)
                .set(field("startDate"), LocalDateTime.now().minusDays(1))
                .set(field("endDate"), LocalDateTime.now().plusDays(1))
                .create());

        var users = List.of(
                userRepository.save(Instancio.of(User.class)
                        .set(field("id"), null)
                        .create()),
                userRepository.save(Instancio.of(User.class)
                        .set(field("id"), null)
                        .create()),
                userRepository.save(Instancio.of(User.class)
                        .set(field("id"), null)
                        .create()),
                userRepository.save(Instancio.of(User.class)
                        .set(field("id"), null)
                        .create())
        );

        // When
        var issues = users.stream()
                .map(user -> CompletableFuture.runAsync(() -> {
                    try {
                        couponFacade.issueCoupon(new IssueCouponCommand(user.getId(), coupon.getId()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }))
                .toList();

        CompletableFuture.allOf(issues.toArray(new CompletableFuture[0])).join();

        // Then
        var updatedCoupon = couponRepository.findCouponById(coupon.getId()).orElseThrow();
        assertEquals(96, updatedCoupon.getStock());
    }


    @Test
    @DisplayName("여러 스레드에서 동시에 쿠폰 발급 요청이 있고, 쿠폰 수가 모자란 경우 초과 발급되지 않아야 한다.")
    void 쿠폰_수가_모자란_경우_초과발급_되지_않아야_한다() {
        // Given
        int remainCoupon = 3;
        var coupon = couponRepository.saveCoupon(Instancio.of(Coupon.class)
                .set(field("id"), null)
                .set(field("stock"), remainCoupon)
                .set(field("startDate"), LocalDateTime.now().minusDays(1))
                .set(field("endDate"), LocalDateTime.now().plusDays(1))
                .create());

        var users = List.of(
                userRepository.save(Instancio.of(User.class)
                        .set(field("id"), null)
                        .create()),
                userRepository.save(Instancio.of(User.class)
                        .set(field("id"), null)
                        .create()),
                userRepository.save(Instancio.of(User.class)
                        .set(field("id"), null)
                        .create()),
                userRepository.save(Instancio.of(User.class)
                        .set(field("id"), null)
                        .create())
        );
        AtomicInteger successCount = new AtomicInteger(0);

        // When
        var issues = users.stream()
                .map(user -> CompletableFuture.runAsync(() -> {
                    try {
                        couponFacade.issueCoupon(new IssueCouponCommand(user.getId(), coupon.getId()));
                        successCount.incrementAndGet();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }))
                .toList();

        CompletableFuture.allOf(issues.toArray(new CompletableFuture[0])).join();

        // Then
        var updatedCoupon = couponRepository.findCouponById(coupon.getId()).orElseThrow();
        assertEquals(0, updatedCoupon.getStock());
        assertEquals(remainCoupon, successCount.get());
    }

}