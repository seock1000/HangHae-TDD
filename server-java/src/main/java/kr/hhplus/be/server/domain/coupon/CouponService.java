package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.coupon.command.CancelCouponCommand;
import kr.hhplus.be.server.domain.coupon.command.IssueCouponCommand;
import kr.hhplus.be.server.domain.coupon.command.UseCouponCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public UserCoupon issue(IssueCouponCommand command) {
        return null;
    }

    public UserCoupon use(UseCouponCommand command) {
        return null;
    }

    public UserCoupon cancel(CancelCouponCommand command) {
        return null;
    }

    public List<UserCouponInfo> getUserCouponsById(Long userId) {
        return null;
    }
}
