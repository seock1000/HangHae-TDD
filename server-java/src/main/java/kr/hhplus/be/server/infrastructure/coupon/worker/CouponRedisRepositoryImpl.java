package kr.hhplus.be.server.infrastructure.coupon.worker;

import kr.hhplus.be.server.application.coupon.IssueCouponCommand;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
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

    @Override
    public void saveIssueCommand(IssueCouponCommand command) {
        String key = couponRedisKeyBuilder.getIssueCommandKeyByCouponId(command.couponId());
        redissonClient.getScoredSortedSet(key).addIfAbsent(System.currentTimeMillis(), command.userId());
    }

    @Override
    public List<Coupon> findAllCoupon() {
        String couponKey = couponRedisKeyBuilder.getCouponKeyPattern();
        return StreamSupport.stream(redissonClient.getKeys().getKeysByPattern(couponKey).spliterator(), false)
                .map(key -> (Coupon) redissonClient.getBucket(key).get())
                .toList();
    }

    @Override
    public Map<Long, List<Long>> getTopIssueRequestPerCouponWithSize(int batchSize) {
        Map<Long, List<Long>> couponIdToUserIds = new ConcurrentHashMap<>();

        String keyPattern = couponRedisKeyBuilder.getIssueCommandKeyPattern();
        redissonClient.getKeys().getKeysByPattern(keyPattern)
                .forEach(key -> {
                    Long couponId = couponRedisKeyBuilder.parseCouponIdFromIssueCommandKey(key);
                    List<Long> userIds = redissonClient.getScoredSortedSet(key).entryRange(0, batchSize)
                            .stream()
                            .map(entry -> (Long) entry.getValue())
                            .collect(Collectors.toList());
                    couponIdToUserIds.put(couponId, userIds);
                });

        return couponIdToUserIds;
    }

    @Override
    public void removeIssueRequest(List<UserCoupon> userCoupons) {
        userCoupons.forEach(userCoupon -> {
            String key = couponRedisKeyBuilder.getIssueCommandKeyByCouponId(userCoupon.getCoupon().getId());
            redissonClient.getScoredSortedSet(key).remove(userCoupon.getUserId());
        });
    }
}
