package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponScheduledFacade {

    private final CouponService couponService;
    private final UserService userService;

    private static final int BATCH_SIZE = 1000;

    // 30초에 1000개씩
    @Scheduled(fixedDelay = 1000 * 30)
    public void issueCouponByBatch() {
        Map<Long, List<Long>> couponIdToUserIds = couponService.getTopIssueRequestsWithSize(BATCH_SIZE);
        List<Coupon> validCoupons = couponService.getValidCoupons();

        validCoupons.forEach(coupon -> {
            List<Long> userIds = couponIdToUserIds.get(coupon.getId());
            if (userIds != null) {
                int issueAmount = Math.min(userIds.size(), BATCH_SIZE);
                List<UserCoupon> userCoupons = userIds.stream()
                        .limit(issueAmount)
                        .map(userId -> {
                            coupon.issue(userService.getUserById(userId));
                            return couponService.issueCoupon(userService.getUserById(userId), coupon);
                        })
                        .toList();
                couponService.removeIssueRequest(userCoupons);
                couponService.saveCoupon(coupon);
            }
        });
    }
}
