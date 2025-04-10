package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.coupon.command.CancelCouponCommand;
import kr.hhplus.be.server.domain.coupon.command.IssueCouponCommand;
import kr.hhplus.be.server.domain.coupon.command.UseCouponCommand;
import kr.hhplus.be.server.domain.coupon.error.AlreadyIssuedCouponError;
import kr.hhplus.be.server.domain.coupon.error.CouponNotExistError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    /**
     * TC
     * 쿠폰이 존재하지 않으면 실패한다. => CouponNotExistError
     * 이미 해당 쿠폰을 발급받았다면 실패한다. => AlreadyIssuedCouponError
     */
    public UserCoupon issue(IssueCouponCommand command) {
        Coupon coupon = couponRepository.findCouponById(command.couponId())
                .orElseThrow(() -> CouponNotExistError.of("쿠폰이 존재하지 않습니다."));

        if (couponRepository.existsUserCouponByUserIdAndCoupon(command.userId(), coupon)) {
            throw AlreadyIssuedCouponError.of("이미 발급받은 쿠폰입니다.");
        }

        return couponRepository.saveUserCoupon(coupon.issueByUserId(command.userId()));
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
