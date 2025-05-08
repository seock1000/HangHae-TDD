package kr.hhplus.be.server.domain.bestseller;

import kr.hhplus.be.server.config.cache.CacheKey;
import kr.hhplus.be.server.config.cache.CacheManagerName;
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

    public void saveBaseAll(List<BestSellerBase> bestSellerBases) {
        bestSellerRepository.saveBaseAll(bestSellerBases);
    }

    public void saveAll(List<BestSeller> bestSellers) {
        bestSellerRepository.saveAll(bestSellers);
    }

    public List<SalesStat> getSalesStatBetween(LocalDate startDate, LocalDate endDate) {
        return bestSellerRepository.getSalesAmountSumBetween(startDate, endDate);
    }

    @Cacheable(value = CacheKey.BEST_SELLERS, cacheManager = CacheManagerName.LOCAL, key = CacheKey.BEST_SELLERS_EL)
    public List<BestSeller> getTodayTop5BestSellers() {
        return bestSellerRepository.getTop5BestSellersByDate(LocalDate.now());
    }

    @Scheduled(cron = "0 0 * * * *") // 예: 매 정각마다 갱신
    @CachePut(value = CacheKey.BEST_SELLERS, cacheManager = CacheManagerName.LOCAL, key = CacheKey.BEST_SELLERS_EL)
    public List<BestSeller> refreshTop5BestSellersCache() {
        return bestSellerRepository.getTop5BestSellersByDate(LocalDate.now());
    }
}
