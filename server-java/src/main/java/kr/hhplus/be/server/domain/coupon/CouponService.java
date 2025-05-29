package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.ApiError;
import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.application.coupon.IssueCouponCommand;
import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final CouponEventPublisher couponEventPublisher;

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

    @Deprecated
    public Coupon getCouponByIdForUpdate(long couponId) {
        return couponRepository.findCouponByIdForUpdate(couponId)
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

    public void saveIssueRequest(IssueCouponCommand command) {
        couponRepository.saveIssueCommand(command);
    }

    public List<Coupon> getValidCoupons() {
        return couponRepository.findAllCoupon().stream()
                .filter(Coupon::isValid)
                .toList();
    }

    public void removeIssueRequest(List<UserCoupon> userCoupons) {
        couponRepository.removeIssueRequest(userCoupons);
    }

    public Map<Long, List<Long>> getTopIssueRequestsWithSize(int batchSize) {
        return couponRepository.getTopIssueRequestPerCouponWithSize(batchSize);
    }

    public void publishIssueEventByCommand(IssueCouponCommand command) {
        couponEventPublisher.publishIssueEvent(command.toEvent());
    }

    public void publishIssuedEvent(CouponEvent.Issued event) {
        couponEventPublisher.publishIssuedEvent(event);
    }

    public void publishIssueFailedEvent(CouponEvent.IssueFailed event) {
        couponEventPublisher.publishIssueFailEvent(event);
    }
}
