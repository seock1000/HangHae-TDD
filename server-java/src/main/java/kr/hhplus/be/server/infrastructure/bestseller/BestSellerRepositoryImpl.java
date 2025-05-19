package kr.hhplus.be.server.infrastructure.bestseller;

import kr.hhplus.be.server.domain.bestseller.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BestSellerRepositoryImpl implements BestSellerRepository {

    private final BestSellerJpaRepository bestSellerJpaRepository;
    private final BestSellerBaseJpaRepository bestSellerBaseJpaRepository;
    private final BestSellerRedisRepository bestSellerRedisRepository;

    @Deprecated
    @Override
    public void saveBaseAll(List<BestSellerBase> bestSellerBases) {
        bestSellerBaseJpaRepository.saveAll(bestSellerBases);
    }

    @Deprecated
    @Override
    public void saveAll(List<BestSeller> bestSellers) {
        bestSellerJpaRepository.saveAll(bestSellers);
    }

    @Deprecated
    @Override
    public List<SalesStat> getSalesAmountSumBetween(LocalDate startDate, LocalDate endDate) {
        return bestSellerBaseJpaRepository.findSalesAmountSumBetween(startDate, endDate);
    }

    @Deprecated
    @Override
    public List<BestSeller> getTop5BestSellersByDate(LocalDate date) {
        return bestSellerJpaRepository.findTop5ByDateOrderBySalesAmountDesc(date);
    }

    @Override
    public void addDailySalesStat(SalesStat salesStat) {
        bestSellerRedisRepository.addDailySalesStat(salesStat);
    }

    @Override
    public List<SalesStat> getDailySalesStatByDate(LocalDate date) {
        return bestSellerRedisRepository.findDailySalesStatByDate(date);
    }

    @Override
    public void addLast3DaysSalesStat(SalesStat salesStat) {
        bestSellerRedisRepository.addLast3DaysSalesStat(salesStat);
    }

    @Override
    public List<SalesStat> getTopLast3DaysSalesStat(int limit) {
        return bestSellerRedisRepository.findTopLast3DaysSalesStat(limit);
    }
}
