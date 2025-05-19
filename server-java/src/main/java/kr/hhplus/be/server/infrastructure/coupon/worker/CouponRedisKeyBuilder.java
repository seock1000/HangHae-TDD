package kr.hhplus.be.server.infrastructure.coupon.worker;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import org.springframework.stereotype.Component;

@Component
public class CouponRedisKeyBuilder {

    public String getCouponKeyByCoupon(Coupon coupon) {
        return "COUPON:" + coupon.getId();
    }

    public String getCouponKeyById(Long couponId) {
        return "COUPON:" + couponId;
    }

    public String getCouponKeyPattern() {
        return "COUPON:*";
    }

    public String getSyncCouponQueueKey() {
        return "COUPON_SYNC_QUEUE";
    }

    public String getIssueHistoryKeyByUserCoupon(UserCoupon userCoupon) {
        return "USER:ISSUE_HISTORY:" + userCoupon.getUserId();
    }

    public String getIssueHistoryKeyByUserId(Long userId) {
        return "USER:ISSUE_HISTORY:" + userId;
    }

    public String getIssueCommandKeyByCouponId(Long couponId) {
        return "COUPON_ISSUE_COMMAND:" + couponId;
    }
    public String getIssueCommandKeyPattern() {
        return "COUPON_ISSUE_COMMAND:*";
    }
    public Long parseCouponIdFromIssueCommandKey(String key) {
        return Long.valueOf(key.split(":")[2]);
    }
}
