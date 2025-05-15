package kr.hhplus.be.server.infrastructure.coupon.worker;

import jakarta.persistence.EntityManager;
import kr.hhplus.be.server.domain.coupon.Coupon;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CouponSyncWorker {

    private final EntityManager entityManager;
    private final CouponRedisKeyBuilder couponRedisKeyBuilder;

    private final RedissonClient redissonClient;
    private static final int BATCH_SIZE = 100;

    // 1초에 100건씩 동기화
    @Scheduled(fixedRate = 1000)
    public void syncUpdateCoupon() {
        redissonClient.getQueue(couponRedisKeyBuilder.getSyncCouponQueueKey())
                .poll(BATCH_SIZE)
                .forEach(coupon -> entityManager.merge((Coupon) coupon));
    }
}
