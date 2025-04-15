package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.UserCouponInfo;
import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {

    private final UserCouponJpaRepository userCouponJpaRepository;
    private final CouponJpaRepository couponJpaRepository;

    @Override
    public List<UserCouponInfo> findUserCouponInfosByUserId(long userId) {
        return userCouponJpaRepository.findByUserId(userId).stream()
                .map(CouponMapper::UserCouponToUserCouponInfo)
                .toList();
    }

    @Override
    public Optional<Coupon> findCouponById(Long couponId) {
        return couponJpaRepository.findById(couponId);
    }

    @Override
    public Optional<UserCoupon> findUserCouponById(Long userCouponId) {
        return userCouponJpaRepository.findById(userCouponId);
    }

    @Override
    public boolean existsUserCouponByUserAndCoupon(User user, Coupon coupon) {
        return userCouponJpaRepository.existsByUserIdAndCouponId(user.getId(), coupon.getId());
    }

    @Override
    public void saveUserCoupon(UserCoupon userCoupon) {
        userCouponJpaRepository.save(userCoupon);
    }

    @Override
    public void saveCoupon(Coupon coupon) {
        couponJpaRepository.save(coupon);
    }
}
