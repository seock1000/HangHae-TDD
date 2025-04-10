package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.coupon.command.IssueCouponCommand;
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

    public UserCoupon use() {
        return null;
    }

    public UserCoupon cancel() {
        return null;
    }

    public List<UserCoupon> getUserCouponsById(Long userId) {
        return null;
    }
}
