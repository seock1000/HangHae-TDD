package kr.hhplus.be.server.domain.coupon;

public class CanceledCoupon {
    private UserCoupon userCoupon;

    private CanceledCoupon(UserCoupon userCoupon) {
        this.userCoupon = userCoupon;
    }

    public static CanceledCoupon of(UserCoupon userCoupon) {
        return new CanceledCoupon(userCoupon);
    }

    public void cancel() {
        userCoupon.init();
    }
}
