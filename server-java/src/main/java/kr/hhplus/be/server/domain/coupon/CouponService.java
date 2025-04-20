package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.ApiError;
import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    /**
     * 테스트 필요없을 듯
     */
    public UserCoupon getUserCouponById(long userCouponId) {
        return couponRepository.findUserCouponById(userCouponId)
                .orElseThrow(() -> ApiException.of(ApiError.COUPON_NOT_FOUND));
    }

    /**
     * 테스트 필요없을 듯
     */
    public Coupon getCouponById(long couponId) {
        return couponRepository.findCouponById(couponId)
                .orElseThrow(() -> ApiException.of(ApiError.COUPON_NOT_FOUND));
    }

    /**
     * 테스트 필요없을 듯
     */
    public List<UserCouponInfo> getUserCouponInfosByUser(GetUserCouponCommand command) {
        return couponRepository.findUserCouponInfosByUserId(command.userId());
    }

    /**
     * TC
     * 이미 발급된 쿠폰이 있으면 COUPON_ALREADY_ISSUED 예외 발생
     */
    public UserCoupon issueCoupon(User user, Coupon coupon) {
        if (couponRepository.existsUserCouponByUserAndCoupon(user, coupon)) {
            throw ApiException.of(ApiError.COUPON_ALREADY_ISSUED);
        }
        return coupon.issue(user);
    }

    /**
     * 테스트 필요없을 듯
     */
    public void saveCoupon(Coupon coupon) {
        couponRepository.saveCoupon(coupon);
    }

    /**
     * 테스트 필요없을 듯
     */
    public void saveUserCoupon(UserCoupon userCoupon) {
        couponRepository.saveUserCoupon(userCoupon);
    }
}
