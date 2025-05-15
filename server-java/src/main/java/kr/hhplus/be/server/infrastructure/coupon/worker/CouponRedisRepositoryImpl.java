package kr.hhplus.be.server.infrastructure.coupon.worker;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Repository
@RequiredArgsConstructor
public class CouponRedisRepositoryImpl implements CouponRedisRepository {

    private final CouponRedisKeyBuilder couponRedisKeyBuilder;
    private final RedissonClient redissonClient;

    @Override
    public Optional<Coupon> findById(Long couponId) {
        String key = couponRedisKeyBuilder.getCouponKeyById(couponId);
        return Optional.ofNullable((Coupon) redissonClient.getBucket(key).get());
    }


    @Override
    public void save(Coupon coupon) {
        String key = couponRedisKeyBuilder.getCouponKeyByCoupon(coupon);
        String syncKey = couponRedisKeyBuilder.getSyncCouponQueueKey();
        redissonClient.getBucket(key).set(coupon);
        redissonClient.getQueue(syncKey).add(coupon); // 쿠폰 동기화 큐에 추가
    }

    @Override
    public void saveIssuedHistory(UserCoupon userCoupon) {
        // 쿠폰 저장시 발급 이력 Set으로 저장
        String issuedHistoryKey = couponRedisKeyBuilder.getIssueHistoryKeyByUserCoupon(userCoupon);
        redissonClient.getSet(issuedHistoryKey).add(userCoupon.getCoupon().getId());
    }

    @Override
    public boolean existsByUserIdAndCouponId(Long userId, Long couponId) {
        String key = couponRedisKeyBuilder.getIssueHistoryKeyByUserId(userId);
        return redissonClient.getSet(key).contains(couponId);
    }
}
