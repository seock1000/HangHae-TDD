package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.command.IssueCouponCommand;
import kr.hhplus.be.server.domain.user.GetUserCommand;
import kr.hhplus.be.server.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponFacade {

    private final CouponService couponService;
    private final UserService userService;

    public IssueCouponResult issueCoupon(IssueCouponCommand command) {
        userService.getUserById(new GetUserCommand(command.userId()));

        return IssueCouponResult.of(couponService.issue(command));
    }
}
