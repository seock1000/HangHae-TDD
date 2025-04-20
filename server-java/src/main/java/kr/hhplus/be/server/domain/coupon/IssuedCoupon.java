package kr.hhplus.be.server.domain.coupon;

public class IssuedCoupon {
    private UserCoupon userCoupon;

    private IssuedCoupon(UserCoupon userCoupon) {
        this.userCoupon = userCoupon;
    }

    protected static IssuedCoupon of(UserCoupon userCoupon) {
        return new IssuedCoupon(userCoupon);
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
