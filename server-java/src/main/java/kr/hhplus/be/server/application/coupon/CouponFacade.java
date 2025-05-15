package kr.hhplus.be.server.application.coupon;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.config.redis.DistributedLock;
import kr.hhplus.be.server.config.redis.LockMethod;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.GetUserCouponCommand;
import kr.hhplus.be.server.domain.coupon.UserCouponInfo;
import kr.hhplus.be.server.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponFacade {

    private final CouponService couponService;
    private final UserService userService;

    @Transactional(readOnly = true)
    public List<UserCouponInfo> getUserCouponInfosByUser(GetUserCouponCommand command) {
        return couponService.getUserCouponInfosByUser(command);
    }

    // 많은 경합 예상 : SPIN 방식 락 적용
    @DistributedLock(key = "'coupon:' + #command.couponId()", method = LockMethod.PUBSUB)
    public IssueCouponResult issueCoupon(IssueCouponCommand command) {
        var user = userService.getUserById(command.userId());
        var coupon = couponService.getCouponById(command.couponId());

        var userCoupon = couponService.issueCoupon(user, coupon);

        couponService.saveCoupon(coupon);
        couponService.saveUserCoupon(userCoupon);
        return IssueCouponResult.of(userCoupon);
    }
}
