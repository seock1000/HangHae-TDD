package kr.hhplus.be.server.domain.bestseller;

import kr.hhplus.be.server.config.cache.CacheKey;
import kr.hhplus.be.server.config.cache.CacheManagerName;
import kr.hhplus.be.server.domain.order.OrderSalesAmount;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BestSellerService {

    private final BestSellerRepository bestSellerRepository;

    @Deprecated
    public void saveBaseAll(List<BestSellerBase> bestSellerBases) {
        bestSellerRepository.saveBaseAll(bestSellerBases);
    }

    @Deprecated
    public void saveAll(List<BestSeller> bestSellers) {
        bestSellerRepository.saveAll(bestSellers);
    }

    @Deprecated
    public List<SalesStat> getSalesStatBetween(LocalDate startDate, LocalDate endDate) {
        return bestSellerRepository.getSalesAmountSumBetween(startDate, endDate);
    }

    @Deprecated
    @Cacheable(value = CacheKey.BEST_SELLERS, cacheManager = CacheManagerName.LOCAL, key = CacheKey.BEST_SELLERS_EL)
    public List<BestSeller> getTodayTop5BestSellers() {
        return bestSellerRepository.getTop5BestSellersByDate(LocalDate.now());
    }

    @Deprecated
    @Scheduled(cron = "0 0 * * * *") // 예: 매 정각마다 갱신
    @CachePut(value = CacheKey.BEST_SELLERS, cacheManager = CacheManagerName.LOCAL, key = CacheKey.BEST_SELLERS_EL)
    public List<BestSeller> refreshTop5BestSellersCache() {
        return bestSellerRepository.getTop5BestSellersByDate(LocalDate.now());
    }

    public void updateDailySalesStat(SalesStat salesStat) {
        bestSellerRepository.addDailySalesStat(salesStat);
    }

    public List<SalesStat> getDailySalesStatByDate(LocalDate date) {
        return bestSellerRepository.getDailySalesStatByDate(date);
    }

    public void updateLast3DaysSalesStat(SalesStat salesStat) {
        bestSellerRepository.addLast3DaysSalesStat(salesStat);
    }

    @Cacheable(value = CacheKey.BEST_SELLERS, cacheManager = CacheManagerName.LOCAL, key = CacheKey.BEST_SELLERS_EL)
    public List<SalesStat> getTop5Last3DaysSalesStat() {
        return bestSellerRepository.getTopLast3DaysSalesStat(5);
    }

}
