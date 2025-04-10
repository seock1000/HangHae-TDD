package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.command.IssueCouponCommand;
import kr.hhplus.be.server.domain.user.GetUserCommand;
import kr.hhplus.be.server.domain.user.UserService;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CouponFacadeTest {
    @Mock
    private CouponService couponService;
    @Mock
    private UserService userService;
    @InjectMocks
    private CouponFacade couponFacade;

    @Test
    @DisplayName("유저를 검증하고 쿠폰을 발급한다.")
    void issueCoupon() {
        // given
        GetUserCommand userCmd = new GetUserCommand(1L);
        IssueCouponCommand couponCmd = new IssueCouponCommand(1L, 1L);
        when(couponService.issue(any(IssueCouponCommand.class)))
                .thenReturn(Instancio.create(UserCoupon.class));

        // when
        IssueCouponResult result = couponFacade.issueCoupon(couponCmd);

        // then
        verify(userService).getUserById(userCmd);
        verify(couponService).issue(couponCmd);

    }


}