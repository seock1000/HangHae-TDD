package kr.hhplus.be.server.infrastructure.bestseller;

import kr.hhplus.be.server.domain.bestseller.SalesStat;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BestSellerRedisRepositoryImpl implements BestSellerRedisRepository {

    private final RedissonClient redissonClient;

    private final BestSellerRedisKeyBuilder bestSellerRedisKeyBuilder;

    @Override
    public void addDailySalesStat(SalesStat salesStat) {
        String key = bestSellerRedisKeyBuilder.getDailyKeyBySalesStat(salesStat);
        redissonClient.getScoredSortedSet(key)
                .addScore(salesStat.getProductId(), salesStat.getAmount()); // 있으면 생성, 없으면 증가
    }

    @Override
    public List<SalesStat> findDailySalesStatByDate(LocalDate date) {
        String key = bestSellerRedisKeyBuilder.getDailyKeyByDate(date);
        RScoredSortedSet<Long> set = redissonClient.getScoredSortedSet(key);

        return set.entryRange(0, -1).stream()
                .map(entry -> new SalesStat(entry.getValue(), ((Number) entry.getScore()).longValue()))
                .toList();
    }

    @Override
    public void addLast3DaysSalesStat(SalesStat salesStat) {
        String key = bestSellerRedisKeyBuilder.getLast3DaysKeyByDate(LocalDate.now());
        redissonClient.getScoredSortedSet(key)
                .addScore(salesStat.getProductId(), salesStat.getAmount()); // 있으면 생성, 없으면 증가
    }

    @Override
    public List<SalesStat> findTopLast3DaysSalesStat(int limit) {
        String key = bestSellerRedisKeyBuilder.getLast3DaysKeyByDate(LocalDate.now());
        RScoredSortedSet<Long> set = redissonClient.getScoredSortedSet(key);

        return set.entryRangeReversed(0, limit - 1)
                .stream()
                .map(entry -> new SalesStat(entry.getValue(), ((Number) entry.getScore()).longValue()))
                .toList();
    }
}
