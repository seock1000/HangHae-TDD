package kr.hhplus.be.server.domain.coupon;

public class AppliedCoupon {
    private UserCoupon userCoupon;

    private AppliedCoupon(UserCoupon userCoupon) {
        this.userCoupon = userCoupon;
    }

    protected static AppliedCoupon of(UserCoupon userCoupon) {
        return new AppliedCoupon(userCoupon);
    }

    public void use() {
        userCoupon.use();
    }

    public int discount(int amount) {
        return userCoupon.getCoupon().discount(amount);
    }

    public Long getId() {
        return userCoupon.getId();
    }
}
