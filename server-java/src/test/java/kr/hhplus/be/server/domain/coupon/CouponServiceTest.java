package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.coupon.command.CancelCouponCommand;
import kr.hhplus.be.server.domain.coupon.command.IssueCouponCommand;
import kr.hhplus.be.server.domain.coupon.command.UseCouponCommand;
import kr.hhplus.be.server.domain.coupon.error.AlreadyIssuedCouponError;
import kr.hhplus.be.server.domain.coupon.error.CouponNotExistError;
import kr.hhplus.be.server.domain.coupon.error.ExpiredCouponError;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;
    @InjectMocks
    private CouponService couponService;

    @Test
    @DisplayName("쿠폰 발급 시, 쿠폰이 존재하지 않으면 CouponNotExistsError 예외가 발생한다.")
    void testIssueCouponNotExistsError() {
        // given
        IssueCouponCommand command = new IssueCouponCommand(1L, 1L);
        when(couponRepository.findCouponById(command.couponId()))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(CouponNotExistError.class, () -> couponService.issue(command));
    }

    @Test
    @DisplayName("쿠폰 발급 시, 이미 발급받은 쿠폰이면 AlreadyIssuedCouponError 예외가 발생한다.")
    void testIssueAlreadyIssuedCouponError() {
        // given
        IssueCouponCommand command = new IssueCouponCommand(1L, 1L);
        Coupon coupon = Instancio.create(Coupon.class);

        when(couponRepository.findCouponById(command.couponId()))
                .thenReturn(Optional.of(coupon));
        when(couponRepository.existsUserCouponByUserIdAndCoupon(command.userId(), coupon))
                .thenReturn(true);

        // when & then
        assertThrows(AlreadyIssuedCouponError.class, () -> couponService.issue(command));
    }

    @Test
    @DisplayName("쿠폰 발급 시, 유효한 요청이면 정상적으로 쿠폰이 발급된다.")
    void testIssueValidCoupon() {
        // given
        IssueCouponCommand command = new IssueCouponCommand(1L, 1L);
        Coupon coupon = Instancio.of(Coupon.class)
                .set(field(Coupon::getEndDate), LocalDateTime.now().plusDays(1))
                .set(field(Coupon::getStock), 1)
                .create();

        when(couponRepository.findCouponById(command.couponId()))
                .thenReturn(Optional.of(coupon));
        when(couponRepository.existsUserCouponByUserIdAndCoupon(command.userId(), coupon))
                .thenReturn(false);
        when(couponRepository.saveUserCoupon(any()))
                .thenReturn(Instancio.create(UserCoupon.class));

        // when
        UserCoupon issuedUserCoupon = couponService.issue(command);

        // then
        verify(couponRepository).findCouponById(command.couponId());
        verify(couponRepository).existsUserCouponByUserIdAndCoupon(command.userId(), coupon);
        verify(couponRepository).saveUserCoupon(any());
    }

    @Test
    @DisplayName("사용자 쿠폰 사용 시, 쿠폰이 존재하지 않으면 CouponNotExistsError 예외가 발생한다.")
    void testUseCouponNotExistsError() {
        // given
        long userCouponId = 1L;
        when(couponRepository.findUserCouponById(userCouponId))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(CouponNotExistError.class, () -> couponService.use(new UseCouponCommand(userCouponId, 100)));
    }

    @Test
    @DisplayName("사용자 쿠폰 사용 시, 쿠폰이 존재하면 사용한다.")
    void testUseValidCoupon() {
        // given
        UseCouponCommand command = new UseCouponCommand(1L, 10000);
        UserCoupon userCoupon = UserCoupon.createWithUserIdAndCoupon(1L,
                Instancio.of(Coupon.class)
                        .set(field(Coupon::getEndDate), LocalDateTime.now().plusDays(1))
                        .create());

        when(couponRepository.findUserCouponById(anyLong()))
                .thenReturn(Optional.of(userCoupon));

        // when
        UserCoupon usedUserCoupon = couponService.use(command);

        // then
        verify(couponRepository).findUserCouponById(command.userCouponId());
        verify(couponRepository).saveUserCoupon(any());
    }

    @Test
    @DisplayName("쿠폰 사용 취소 시, 쿠폰이 존재하지 않으면 CouponNotExistsError 예외가 발생한다.")
    void testCancelCouponNotExistsError() {
        // given
        long userCouponId = 1L;
        when(couponRepository.findUserCouponById(userCouponId))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(CouponNotExistError.class, () -> couponService.cancel(new CancelCouponCommand(userCouponId)));
    }

    @Test
    @DisplayName("쿠폰 사용 취소 시, 쿠폰이 존재하면 사용 취소한다.")
    void testCancelValidCoupon() {
        // given
        CancelCouponCommand command = new CancelCouponCommand(1L);
        UserCoupon userCoupon = Instancio.create(UserCoupon.class);

        when(couponRepository.findUserCouponById(anyLong()))
                .thenReturn(Optional.of(userCoupon));

        // when
        couponService.cancel(command);

        // then
        verify(couponRepository).findUserCouponById(command.userCouponId());
        verify(couponRepository).saveUserCoupon(any());
    }

}