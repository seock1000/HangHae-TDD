package kr.hhplus.be.server.infrastructure.coupon.worker;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.UserCouponInfo;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.infrastructure.coupon.persistence.CouponJpaRepository;
import kr.hhplus.be.server.infrastructure.coupon.persistence.UserCouponJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {

    private final UserCouponJpaRepository userCouponJpaRepository;
    private final CouponJpaRepository couponJpaRepository;
    private final CouponRedisRepository couponRedisRepository;

    @Override
    public List<UserCouponInfo> findUserCouponInfosByUserId(long userId) {
        return userCouponJpaRepository.findByUserId(userId).stream()
                .map(CouponMapper::UserCouponToUserCouponInfo)
                .toList();
    }

    @Override
    public Optional<Coupon> findCouponById(Long couponId) {
        return couponRedisRepository.findById(couponId);
    }

    @Deprecated
    @Override
    public Optional<Coupon> findCouponByIdForUpdate(Long couponId) {
        return couponJpaRepository.findForUpdate(couponId);
    }

    @Override
    public Optional<UserCoupon> findUserCouponById(Long userCouponId) {
        return userCouponJpaRepository.findById(userCouponId);
    }

    @Override
    public boolean existsUserCouponByUserAndCoupon(User user, Coupon coupon) {
        return couponRedisRepository.existsByUserIdAndCouponId(user.getId(), coupon.getId());
    }

    @Override
    public UserCoupon saveUserCoupon(UserCoupon userCoupon) {
        userCoupon = userCouponJpaRepository.save(userCoupon);
        couponRedisRepository.saveIssuedHistory(userCoupon);
        return userCoupon;
    }

    @Override
    public Coupon saveCoupon(Coupon coupon) {
        if(coupon.getId() == null) {
            // 신규 쿠폰인 경우 DB에 저장 후 Redis에 저장
            coupon = couponJpaRepository.save(coupon);
            couponRedisRepository.save(coupon);
        }
        // 쿠폰이 이미 존재하는 경우 Redis에 저장
        else {
            couponRedisRepository.save(coupon);
        }
        return coupon;
    }
}
